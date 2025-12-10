package org.senai.cantina_vidal.config;

import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.enums.Role;
import org.senai.cantina_vidal.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminUserConfig implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@cantina.com";
        User userAdmin = userRepository.findByEmail(adminEmail).orElseGet(() ->
                User.builder()
                        .name("VidalTester")
                        .email(adminEmail)
                        .passwordHash(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .emailVerified(true)
                        .build()
        );

        userRepository.save(userAdmin);
        System.out.println("Usuário " + userAdmin.getName() + " Salvo com sucesso no banco!");
    }
}
