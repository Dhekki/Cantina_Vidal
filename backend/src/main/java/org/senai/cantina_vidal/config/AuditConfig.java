package org.senai.cantina_vidal.config;

import org.senai.cantina_vidal.security.UserPrincipal;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Objects;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null ||
                    !authentication.isAuthenticated() ||
                    Objects.equals(authentication.getPrincipal(), "anonymousUser")) {
                return Optional.empty();
            }

            Object principal = authentication.getPrincipal();
            if (principal instanceof UserPrincipal userPrincipal) {
                return Optional.of(userPrincipal.getUser().getId());
            }

            return Optional.empty();
        };
    }
}
