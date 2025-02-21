/* package com.marco.authorizationservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:" + serverPort);
        devServer.setDescription("Serveur de développement");

        Contact contact = new Contact();
        contact.setName("Marco");
        contact.setEmail("marcfrt@gmail.com");

        Info info = new Info()
                .title("API de Service d'Autorisation")
                .version("1.0")
                .contact(contact)
                .description("Cette API gère les autorisations des communications inter-services.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
} */