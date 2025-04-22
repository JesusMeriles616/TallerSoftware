package com.example.keycloak.config;



import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Instant;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/**", "/users/register", "/swagger-ui.html", "/swagger-ui/**",
            "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**", "/actuator/health"
    };

    private static final String[] PROFILE_ENDPOINTS = {"/perfil/**"};

    private static final String[] GERENTE_ENDPOINTS = {"/v1/admin/**", "/actuator/**"};

    private static final String[] ROLES_COMUNES = {
            "NIÑO", "ADMINISTRADOR", "PADRE"//modificar
    };
    
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, PROFILE_ENDPOINTS).hasAnyRole(ROLES_COMUNES)
                        .requestMatchers(HttpMethod.PUT, PROFILE_ENDPOINTS).hasAnyRole(ROLES_COMUNES)
                        .requestMatchers(HttpMethod.DELETE, PROFILE_ENDPOINTS).hasAnyRole("CLIENTE", "GERENTE")
                        .requestMatchers(GERENTE_ENDPOINTS).hasRole("GERENTE")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e->e.accessDeniedHandler(accessDeniedHandler()))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                );

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "X-Requested-With", "Accept", "X-XSRF-TOKEN"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(String.format("""
                {
                    "error": "Forbidden",
                    "message": "Access Denied: %s",
                    "path": "%s",
                    "timestamp": "%s",
                    "requiredRoles": "%s"
                }
                """,
                    accessDeniedException.getMessage(),
                    request.getRequestURI(),
                    Instant.now(),
                    getRequiredRoles(request)
            ));
        };
    }
    private String getRequiredRoles(HttpServletRequest request){
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (uri.contains("/perfil")) {
            return method.equals("DELETE")
                    ? "NIÑO or PADRE"
                    : "NIÑO, PADRE,ADMINISTRADOR";
        } else if (uri.contains("/v1/admin")) {
            return "PADRE";
        }
        return "Authenticated user";
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/error", "/favicon.ico", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**"
        );
    }
    @Bean
    public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
       return NimbusJwtDecoder.withJwkSetUri(issuerUri + "/protocol/openid-connect/certs").build();
    }
}
