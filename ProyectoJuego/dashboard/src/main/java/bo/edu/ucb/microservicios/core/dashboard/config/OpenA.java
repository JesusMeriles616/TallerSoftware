package bo.edu.ucb.microservicios.core.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenA {

        @Bean
        public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("Dashboard API")
                .version("1.0")
                .description("API para gestión de rendimiento de niños")
                .contact(new Contact().name("Vladimir Douglas Gutierrez Oropeza").email("gutierrez.oropeza.vladimir@email.com"))
                );
        }
}
