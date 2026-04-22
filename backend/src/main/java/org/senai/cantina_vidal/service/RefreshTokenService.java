package org.senai.cantina_vidal.service;

import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.entity.RefreshToken;
import org.senai.cantina_vidal.repository.RefreshTokenRepository;
import org.senai.cantina_vidal.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.UUID;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {
    private final RefreshTokenRepository repository;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenDuration;

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenDuration / 1000))
                .build();

        return repository.save(refreshToken);
    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            repository.delete(token);
            throw new BadCredentialsException("Refresh Token expirado. Faça login novamente.");
        }
        return token;
    }

    public RefreshToken findByToken(String token) {
        return repository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh Token não encontrado"));
    }

    @Transactional
    public void revokeToken(String token) {
        repository.deleteByToken(token);
    }
}
