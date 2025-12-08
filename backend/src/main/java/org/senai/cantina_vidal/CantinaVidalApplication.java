package org.senai.cantina_vidal;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@OpenAPIDefinition(
        info = @Info(
                title = "Cantina Vidal",
                description = "Api do sistema da Cantina Vidal",
                version = "1.0"
        )
)
public class CantinaVidalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CantinaVidalApplication.class, args);
    }

}
