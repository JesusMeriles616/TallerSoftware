package com.example.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Libraries APIS", version = "1.0", description = "Libreria Manual de Apis."))
public class KeycloakApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(KeycloakApplication.class, args);
	}

}
