package org.senai.cantina_vidal.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Cantina Vidal API",
                version = "1.0",
                description = "API para gestão de pedidos e cardápio de Cantina/Restaurante."
        )
)
@SecurityScheme(
        name = "bearer-key",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Insira o token JWT no formato: Bearer {token}"
)
public class OpenApiConfig {}
