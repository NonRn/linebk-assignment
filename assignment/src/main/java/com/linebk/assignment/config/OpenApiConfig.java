package com.linebk.assignment.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "LineBK Assignment API",
        version = "v1",
        description = "API documentation for the LineBK assignment services.",
        contact = @Contact(name = "LineBK API Support", email = "support@linebk.com")
    ),
    servers = {
        @Server(url = "/", description = "Default server")
    }
)
@Configuration
public class OpenApiConfig {
}
