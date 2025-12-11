package org.senai.cantina_vidal.service;

import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.dto.UserResponseDTO;
import org.senai.cantina_vidal.dto.auth.LoginRequestDTO;
import org.senai.cantina_vidal.dto.auth.LoginResponseDTO;
import org.senai.cantina_vidal.dto.auth.RegisterRequestDTO;
import org.senai.cantina_vidal.entity.RefreshToken;
import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.enums.Role;
import org.senai.cantina_vidal.exception.ConflictException;
import org.senai.cantina_vidal.repository.RefreshTokenRepository;
import org.senai.cantina_vidal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailService emailService;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenDuration;

    @Transactional
    public User register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email()))
                throw new ConflictException("Email já cadastrado");

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .role(Role.CLIENT)
                .build();

        user.setPasswordHash(passwordEncoder.encode(dto.password()));

        String verificationCode = String.valueOf(secureRandom.nextInt(900000) + 100000); // Between 100.000 and 999.999

//        emailService.composeEmail(verificationCode, dto.email());

        user.setVerificationCode(verificationCode);
        user.setVerificationExpiresAt(LocalDateTime.now().plusSeconds(900)); // 15 minutes

        return userRepository.save(user);
    }

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(dto.password(), user.getPasswordHash()))
            throw new BadCredentialsException("Email ou senha inválidos");


        String accessToken = tokenService.generateAccessToken(user);

        String refreshTokenString = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenString)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenDuration / 1000)) // 30 days
                .build();

        refreshTokenRepository.deleteByUserId(user.getId()); // Validate need afterwards

        refreshTokenRepository.save(refreshToken);

        return new LoginResponseDTO(accessToken, refreshTokenString, user.getName(), user.getRole().name());
    }

    public LoginResponseDTO refreshToken(String requestRefreshToken) {
        RefreshToken entity = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh Token não encontrado"));

        if (entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(entity);
            throw new RuntimeException("Refresh Token expirado. Faça login novamente");
        }

        String newAccessToken = tokenService.generateAccessToken(entity.getUser());

        return new LoginResponseDTO(newAccessToken, requestRefreshToken, entity.getUser().getName(), entity.getUser().getRole().name());
    }
}
