package com.example.keycloak.config;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.jwt.Jwt;

//Java Coleection
import java.time.Instant;

import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
// lombok
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    /*** Declaración de constantes y dependencias ***/
    private static final String DEFAULT_AUTHORITY_PREFIX = "ROLE_";
    private static final String DEFAULT_AUTH_CLAIM = "realm_access.roles";

    private final JwtGrantedAuthoritiesConverter defaultAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final Map<String, CachedAuthorities> authorityCache = new ConcurrentHashMap<>();

    /*** Inyección de propiedades desde application.properties o application.yml ***/
    @Value("${jwt.auth.converter.principle-attribute:preferred_username}")
    private String principleAttribute;

    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    @Value("${jwt.auth.converter.enable-cache:false}")
    private boolean enableCache;

    @Value("${jwt.auth.converter.cache-ttl:300}")
    private long cacheTtl;

    /*** Inicialización de configuraciones al arrancar el componente ***/
    @PostConstruct
    public void init() {
        defaultAuthoritiesConverter.setAuthorityPrefix(DEFAULT_AUTHORITY_PREFIX);
        defaultAuthoritiesConverter.setAuthoritiesClaimName(DEFAULT_AUTH_CLAIM);
        log.info("JWT Authentication Converter configurado para resource-id: {}", resourceId);
    }

    /*** Conversión principal del JWT a AbstractAuthenticationToken ***/
    @NotNull
    @Override
    public AbstractAuthenticationToken convert(@NotNull Jwt jwt) {
        validateJwt(jwt);

        Collection<GrantedAuthority> authorities = enableCache
                ? getCachedAuthorities(jwt)
                : combineAuthorities(jwt);

        return new JwtAuthenticationToken(jwt, authorities, resolvePrincipal(jwt));
    }
    /*** Combina roles de distintas fuentes: realm, client, directos y personalizados ***/
    private Collection<GrantedAuthority> combineAuthorities(Jwt jwt) {
        try {
            Collection<GrantedAuthority> authorities = Stream.of(
                            defaultAuthoritiesConverter.convert(jwt),
                            extractClientRoles(jwt),
                            extractDirectRoles(jwt),
                            extractCustomAuthorities(jwt)
                    )
                    .filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            if (log.isDebugEnabled()) {
                String joinedAuthorities = authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(", "));
                log.debug("Autoridades extraídas para {}: {}", resolvePrincipal(jwt), joinedAuthorities);
            }

            return Collections.unmodifiableCollection(authorities);
        } catch (Exception e) {
            log.error("Error combinando autoridades del JWT", e);
            return Set.of();
        }
    }

    /*** Obtiene autoridades desde caché o las recalcula si están expiradas ***/
    private Collection<GrantedAuthority> getCachedAuthorities(Jwt jwt) {
        return authorityCache.compute(jwt.getTokenValue(), (key, cached) ->
                (cached == null || cached.isExpired()) ? new CachedAuthorities(combineAuthorities(jwt)) : cached
        ).getAuthorities();
    }

    private String resolvePrincipal(Jwt jwt) {
        return Stream.of(principleAttribute, "preferred_username", "email", "sub")
                .map(jwt::getClaimAsString)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se pudo determinar el principal del JWT"));
    }

    /*** Extrae roles del cliente desde resource_access.{clientId}.roles ***/
    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractClientRoles(Jwt jwt) {
        try {
            Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaim("resource_access");
            Map<String, Object> clientData = (Map<String, Object>) Optional.ofNullable(resourceAccess)
                    .map(m -> m.get(resourceId))
                    .orElse(null);

            List<?> roles = (List<?>) Optional.ofNullable(clientData)
                    .map(m -> ((Map<?, ?>) m).get("roles"))
                    .orElse(null);

            return mapRolesToAuthorities(roles);
        } catch (Exception e) {
            log.warn("Error extrayendo roles de cliente para {}", resourceId, e);
            return Set.of();
        }
    }
    /*** Extrae roles directos desde el claim 'roles' ***/
    private Collection<GrantedAuthority> extractDirectRoles(Jwt jwt) {
        try {
            List<?> roles = (List<?>) jwt.getClaim("roles");
            return mapRolesToAuthorities(roles);
        } catch (Exception e) {
            log.debug("No se encontraron roles directos en el JWT");
            return Set.of();
        }
    }

    /**
     * Utilidad común para convertir roles a GrantedAuthority
     */
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<?> roles) {
        if (roles == null) return Set.of();
        return roles.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(role -> DEFAULT_AUTHORITY_PREFIX + role.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /*** Método de extensión para extraer autoridades personalizadas ***/
    protected Collection<GrantedAuthority> extractCustomAuthorities(Jwt jwt) {
        return Set.of();
    }
    /*** Validación básica del JWT: token no vacío y no expirado ***/
    private void validateJwt(Jwt jwt) {
        if (jwt.getTokenValue() == null || jwt.getTokenValue().isEmpty()) {
            throw new IllegalArgumentException("Token JWT vacío o inválido");
        }
        if (jwt.getExpiresAt() != null && jwt.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token JWT expirado");
        }
    }
    /*** Limpia manualmente el caché de autoridades ***/
    public void clearAuthorityCache() {
        authorityCache.clear();
    }
    /*** Clase interna para almacenar autoridades con TTL ***/
    private class CachedAuthorities {
        private final Collection<GrantedAuthority> authorities;
        private final long creationTime = System.currentTimeMillis();

        CachedAuthorities(Collection<GrantedAuthority> authorities) {
            this.authorities = authorities;
        }

        Collection<GrantedAuthority> getAuthorities() {
            return authorities;
        }

        boolean isExpired() {
            return (System.currentTimeMillis() - creationTime) > TimeUnit.SECONDS.toMillis(cacheTtl);
        }
    }
}
