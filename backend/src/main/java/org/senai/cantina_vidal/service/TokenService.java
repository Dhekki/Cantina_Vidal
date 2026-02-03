package org.senai.cantina_vidal.service;

import lombok.extern.slf4j.Slf4j;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.senai.cantina_vidal.entity.User;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import org.senai.cantina_vidal.exception.InvalidTokenException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

@Slf4j
@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    public String generateAccessToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("CantinaVidal")
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withClaim("role", user.getRole().name())
                    .withExpiresAt(Instant.now().plusMillis(expirationTime))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            log.error("Erro crítico ao gerar token para o usuário: {}", user.getEmail(), e);
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("CantinaVidal")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException e) {
            throw new InvalidTokenException("Token expirado");
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("Token inválido ou malformado");
        }
    }
}
