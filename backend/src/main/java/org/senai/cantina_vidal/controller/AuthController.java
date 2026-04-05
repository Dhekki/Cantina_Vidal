package org.senai.cantina_vidal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.controller.api.AuthApi;
import org.senai.cantina_vidal.dto.auth.*;
import org.senai.cantina_vidal.service.AuthService;
import org.senai.cantina_vidal.dto.user.UserResponseDTO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthApi {
    private final AuthService service;

    @Value("${cantina.security.cookie-secure:false}")
    private boolean isCookieSecure;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMs;

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        LoginProcessDTO loginData = service.login(data);

        ResponseCookie accessTokenCookie = createSecureCookie("accessToken", loginData.accessToken(), jwtExpirationMs);
        
        ResponseCookie refreshTokenCookie = createSecureCookie("refreshToken", loginData.refreshToken(), refreshExpirationMs);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new LoginResponseDTO(loginData.name(), loginData.role()));
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid RegisterRequestDTO data) {
        UserResponseDTO user = new UserResponseDTO(service.register(data));
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("{/users/id}")
                .buildAndExpand(user.id())
                .toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {
        LoginProcessDTO loginData = service.refreshToken(refreshToken);

        ResponseCookie newAccessTokenCookie = createSecureCookie("accessToken", loginData.accessToken(), jwtExpirationMs);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString())
                .body(new LoginResponseDTO(loginData.name(), loginData.role()));
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            service.revokeRefreshToken(refreshToken);
        }

        ResponseCookie deleteAccess = createSecureCookie("accessToken", "", 0);
        ResponseCookie deleteRefresh = createSecureCookie("refreshToken", "", 0);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteAccess.toString())
                .header(HttpHeaders.SET_COOKIE, deleteRefresh.toString())
                .build();
    }

    private ResponseCookie createSecureCookie(String name, String value, long maxAgeMs) {
        long maxAgeSeconds = maxAgeMs / 1000;

        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(isCookieSecure)
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite("Strict")
                .build();
    }
}
