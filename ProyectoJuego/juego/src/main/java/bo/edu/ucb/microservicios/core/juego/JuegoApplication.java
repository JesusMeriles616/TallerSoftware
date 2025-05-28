package bo.edu.ucb.microservicios.core.juego;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class JuegoApplication {
    public static void main(String[] args) {
        SpringApplication.run(JuegoApplication.class, args);
    }
} 