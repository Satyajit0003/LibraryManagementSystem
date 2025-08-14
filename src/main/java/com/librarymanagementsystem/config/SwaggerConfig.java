package com.librarymanagementsystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig() {
        return new OpenAPI()
                .info(
                        new Info().title("LibraryManagementSystem APIs")
                                .description("API documentation for the Library Management System")
                                .version("1.0.0")
                )
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080/library/v1").description("Local Server")
                ))
                .tags(Arrays.asList(
                        new Tag().name("Public APIs").description("APIs accessible without authentication"),
                        new Tag().name("Admin APIs").description("APIs for admin operations"),
                        new Tag().name("User APIs").description("APIs for user operations"),
                        new Tag().name("Librarian APIs").description("APIs for book management")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));
    }
}
