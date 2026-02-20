package org.senai.cantina_vidal.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
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
        name = "cookieAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "accessToken",
        description = "A autenticação é feita via HttpOnly Cookies de forma automática. Faça login no endpoint /auth/login e o navegador cuidará do resto."
)
public class OpenApiConfig {}
