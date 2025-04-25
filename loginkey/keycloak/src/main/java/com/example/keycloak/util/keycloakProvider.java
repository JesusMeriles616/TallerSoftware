package com.example.keycloak.util;



import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
/***
 * Configuración y proveedor de clientes Keycloak para la aplicación.
 * Proporciona métodos para crear instancias de clientes Keycloak con diferentes flujos de autenticación.
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class keycloakProvider {
    private final KeycloakConfig keycloakConfig;
    private Keycloak keycloakAdminInstance;
    private Keycloak keycloakUserInstance;
    /***
     * Inicializa las instancias de Keycloak al cargar el bean.
     */
    @PostConstruct
    public void init() {
        this.keycloakAdminInstance = createKeycloakAdminInstance();
        this.keycloakUserInstance = createKeycloakUserInstance();
    }
    /***
     * Cierra las conexiones de Keycloak al destruir el bean.
     */
    @PreDestroy
    public void close() {
        closeQuietly(keycloakAdminInstance);
        closeQuietly(keycloakUserInstance);
    }
    /***
     * Crea y configura un cliente Keycloak básico con credenciales de cliente.
     * @return Instancia configurada de Keycloak
     */
    @Bean
    public Keycloak keycloak() {
        return createClient(
                keycloakConfig.getRealm(),
                keycloakConfig.getResource(),
                keycloakConfig.getCredentials().getSecret(),
                OAuth2Constants.CLIENT_CREDENTIALS,
                null,
                null,
                "Keycloak client"
        );
    }
    /***
     * Crea una instancia de Keycloak con privilegios de administrador.
     * @return Instancia de Keycloak configurada como admin
     */
    public Keycloak createKeycloakAdminInstance() {
        return createClient(
                keycloakConfig.getRealmMaster(),
                "admin-cli",
                null,
                null,
                keycloakConfig.getAdmin().getUsername(),
                keycloakConfig.getAdmin().getPassword(),
                "Keycloak admin client"
        );
    }
    /***
     * Crea una instancia de Keycloak con credenciales de usuario.
     * @return Instancia de Keycloak configurada como usuario
     */
    public Keycloak createKeycloakUserInstance() {
        return createClient(
                keycloakConfig.getRealm(),
                keycloakConfig.getResource(),
                keycloakConfig.getCredentials().getSecret(),
                OAuth2Constants.PASSWORD,
                keycloakConfig.getAdmin().getUsername(),
                keycloakConfig.getAdmin().getPassword(),
                "Keycloak user client"
        );
    }
    public Keycloak getUserKeycloakInstance(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("Username and password cannot be blank");
        }

        log.info("Creating Keycloak instance for user: {}", username);

        try {
            ResteasyClient client = new ResteasyClientBuilderImpl()
                    .connectTimeout(keycloakConfig.getConnection().getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(keycloakConfig.getConnection().getReadTimeout(), TimeUnit.MILLISECONDS)
                    .build();

            return KeycloakBuilder.builder()
                    .serverUrl(keycloakConfig.getAuthServerUrl())
                    .realm(keycloakConfig.getRealm())
                    .clientId(keycloakConfig.getResource())
                    .clientSecret(keycloakConfig.getCredentials().getSecret())
                    .username(username)
                    .password(password)
                    .grantType(OAuth2Constants.PASSWORD)
                    .resteasyClient(client)
                    .build();
        } catch (Exception e) {
            log.error("Failed to authenticate user: {}", username, e);
            throw new KeycloakServiceException("Authentication failed for user: " + username, e);
        }
    }
    /**
     * Obtiene una instancia de Keycloak configurada para usar un refresh token
     * @param refreshToken El refresh token válido
     * @return Instancia de Keycloak configurada para refresh token
     * @throws IllegalArgumentException Si el refresh token está vacío
     * @throws KeycloakServiceException Si hay error en la configuración
     */
    public Keycloak getRefreshTokenInstance(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            throw new IllegalArgumentException("Refresh token cannot be blank");
        }
        log.debug("Creating Keycloak instance for refresh token");
        try {
            // Configuración manual del flujo de refresh token
            Map<String, Object> clientCredentials = new HashMap<>();
            clientCredentials.put(OAuth2Constants.REFRESH_TOKEN, refreshToken);
            clientCredentials.put(OAuth2Constants.CLIENT_ID, keycloakConfig.getResource());
            clientCredentials.put(OAuth2Constants.CLIENT_SECRET, keycloakConfig.getCredentials().getSecret());
            return KeycloakBuilder.builder()
                    .serverUrl(keycloakConfig.getAuthServerUrl())
                    .realm(keycloakConfig.getRealm())
                    .grantType(OAuth2Constants.REFRESH_TOKEN)
                    .authorization("Bearer " + refreshToken)
                    .resteasyClient(buildResteasyClient())
                    .build();
        } catch (Exception e) {
            log.error("Failed to create refresh token instance", e);
            throw new KeycloakServiceException("Failed to configure refresh token flow", e);
        }
    }

    /***
     * Método interno para crear clientes Keycloak con diferentes configuraciones.
     * @param realm Realm de Keycloak
     * @param clientId ID del cliente
     * @param clientSecret Secreto del cliente
     * @param grantType Tipo de concesión OAuth2
     * @param username Nombre de usuario (opcional)
     * @param password Contraseña (opcional)
     * @param clientDescription Descripción para logs
     * @return Instancia configurada de Keycloak
     * @throws KeycloakServiceException Si falla la creación del cliente
     */
    private Keycloak createClient(String realm, String clientId, String clientSecret,
                                  String grantType, String username, String password,
                                  String clientDescription) {
        log.info("Initializing {} for realm: {}", clientDescription, realm);

        try {
            return KeycloakBuilder.builder()
                    .serverUrl(keycloakConfig.getAuthServerUrl())
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType(grantType)
                    .username(username)
                    .password(password)
                    .resteasyClient(buildResteasyClient())
                    .build();
        } catch (Exception e) {
            log.error("Failed to initialize {}", clientDescription, e);
            throw new KeycloakServiceException(clientDescription + " initialization failed", e);
        }
    }
    //Metodo Auxiliares
    /***
     * Construye un cliente REST para las conexiones Keycloak.
     * @return Cliente REST configurado
     */
    private ResteasyClient buildResteasyClient() {
        return new ResteasyClientBuilderImpl()
                .connectTimeout(keycloakConfig.getConnection().getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(keycloakConfig.getConnection().getReadTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }
    /***
     * Cierra una instancia de Keycloak manejando posibles errores.
     * @param keycloakInstance Instancia a cerrar
     */
    private void closeQuietly(Keycloak keycloakInstance) {
        if (keycloakInstance != null) {
            try {
                keycloakInstance.close();
            } catch (Exception e) {
                log.warn("Error closing Keycloak instance", e);
            }
        }
    }
    // Métodos para acceder a recursos
    /***
     * Obtiene el recurso del realm configurado.
     * @return Recurso del realm
     */
    public RealmResource getRealmResource() {
        return keycloakAdminInstance.realm(keycloakConfig.getRealm());
    }

    /***
     * Obtiene el recurso de usuarios del realm.
     * @return Recurso de usuarios
     */
    public UsersResource getUserResource() {
        return getRealmResource().users();
    }
    /***
     * Obtiene el nombre del realm configurado.
     * @return Nombre del realm
     */
    public String getRealm() {
        return keycloakConfig.getRealm();
    }
}
