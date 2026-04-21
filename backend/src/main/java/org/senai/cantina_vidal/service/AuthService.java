package org.senai.cantina_vidal.service;

import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.dto.auth.LoginProcessDTO;
import org.senai.cantina_vidal.enums.Role;
import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.entity.RefreshToken;
import org.senai.cantina_vidal.dto.auth.LoginRequestDTO;
import org.senai.cantina_vidal.repository.UserRepository;
import org.senai.cantina_vidal.dto.auth.RegisterRequestDTO;
import org.senai.cantina_vidal.exception.ConflictException;
import org.senai.cantina_vidal.exception.ResourceNotFoundException;
import org.senai.cantina_vidal.repository.RefreshTokenRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;

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
        String encodedPassword = passwordEncoder.encode(dto.password());
        LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(900); // 15 minutes
        String verificationCode = String.valueOf(secureRandom.nextInt(900000) + 100000); // Between 100.000 and 999.999

//        emailService.composeEmail(verificationCode, dto.email());

        User user = User.builder()
                .name(dto.name())
                .role(Role.CLIENT)
                .email(dto.email())
                .passwordHash(encodedPassword)
                .verificationCode(verificationCode)
                .verificationExpiresAt(expirationTime)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public LoginProcessDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmailAndDeletedFalse(dto.email())
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(dto.password(), user.getPasswordHash()))
            throw new BadCredentialsException("Email ou senha inválidos");


        String accessToken = tokenService.generateAccessToken(user);

        String refreshTokenString = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenString)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenDuration / 1000))
                .build();

        refreshTokenRepository.save(refreshToken);

        return new LoginProcessDTO(accessToken, refreshTokenString, user.getName(), user.getRole().name());
    }

    public LoginProcessDTO refreshToken(String refreshToken) {
        RefreshToken entity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh Token não encontrado"));

        if (entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(entity);
            throw new BadCredentialsException("Refresh Token expirado. Faça login novamente");
        }

        String newAccessToken = tokenService.generateAccessToken(entity.getUser());

        return new LoginProcessDTO(newAccessToken, refreshToken, entity.getUser().getName(), entity.getUser().getRole().name());
    }

    public void revokeRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
