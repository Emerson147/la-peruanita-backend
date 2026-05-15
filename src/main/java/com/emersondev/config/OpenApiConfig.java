package com.emersondev.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "DenRaf API",
                version = "1.0",
                description = "Backend de gestión de inventario y ventas DenRaf"
        )
)
public class OpenApiConfig {
}