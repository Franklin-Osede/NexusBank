package com.nexusbank.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @Bean
    public OpenAPI nexusBankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NexusBank API")
                        .description("API para la gestión de cuentas bancarias, usuarios y transacciones")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("NexusBank Development Team")
                                .email("developers@nexusbank.com")
                                .url("https://nexusbank.com/support"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080" + contextPath)
                                .description("Desarrollo local"),
                        new Server()
                                .url("https://api.nexusbank.com" + contextPath)
                                .description("Servidor de producción (simulado)")))
                .tags(Arrays.asList(
                        new Tag().name("users").description("Operaciones relacionadas con usuarios"),
                        new Tag().name("accounts").description("Operaciones relacionadas con cuentas bancarias"),
                        new Tag().name("transactions")
                                .description("Operaciones relacionadas con transacciones bancarias")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description(
                                                "JWT token de autenticación. Usar el endpoint /api/auth/login para obtener el token.")));
    }
}