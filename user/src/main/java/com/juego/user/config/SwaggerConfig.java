package com.juego.user.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.GroupedOpenApi;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("usuarios")
                .packagesToScan("com.juegos.user") // Cambia esto al paquete donde están tus controladores
                .build();
    }
}
