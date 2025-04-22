package com.example.keycloak.util;



import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakConfig {
    // Propiedades de configuración
    private String authServerUrl;
    private String realm;
    private String realmMaster;
    private String resource;
    private boolean useResourceRoleMappings;
    private String sslRequired;
    private int confidentialPort;
    private final Credentials credentials = new Credentials();
    private final Admin admin = new Admin();
    private final Connection connection = new Connection();

    // Clases anidadas para propiedades
    @Getter
    @Setter
    public static class Credentials {
        private String secret;
    }

    @Getter
    @Setter
    public static class Admin {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class Connection {
        private int connectTimeout;
        private int readTimeout;
    }
}
