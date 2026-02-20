package org.senai.cantina_vidal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.senai.cantina_vidal.dto.auth.*;
import org.senai.cantina_vidal.service.AuthService;
import org.senai.cantina_vidal.service.TokenService;
import org.senai.cantina_vidal.dto.user.UserResponseDTO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "1. Autenticação", description = "Endpoints para login/cadastro e gestão de tokens")
public class AuthController {
    private final AuthService service;
    private final TokenService tokenService;

    @Value("${cantina.security.cookie-secure:false}")
    private boolean isCookieSecure;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMs;

    @Operation(summary = "Realizar Login", description = "Autentica um usuário e retorna tokens de acesso e refresh.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de login inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciais incorretas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        LoginProcessDTO loginData = service.login(data);

        ResponseCookie accessTokenCookie = createSecureCookie("accessToken", loginData.accessToken(), jwtExpirationMs);
        ResponseCookie refreshTokenCookie = createSecureCookie("refreshToken", loginData.refreshToken(), jwtExpirationMs);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new LoginResponseDTO(loginData.name(), loginData.role()));
    }

    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de cliente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (Senha curta, email malformado)"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid RegisterRequestDTO data) {
        UserResponseDTO user = new UserResponseDTO(service.register(data));
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("{/users/id}")
                .buildAndExpand(user.id())
                .toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @Operation(summary = "Atualizar Token (Refresh)", description = "Gera um novo Access Token usando um Refresh Token válido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token renovado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Formato do token inválido"),
            @ApiResponse(responseCode = "401", description = "Refresh Token expirado (Faça login novamente)"),
            @ApiResponse(responseCode = "404", description = "Refresh Token não encontrado no banco (Revogado ou inexistente)")
    })
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {
        LoginProcessDTO loginData = service.refreshToken(refreshToken);

        ResponseCookie newAccessTokenCookie = createSecureCookie("accessToken", loginData.accessToken(), jwtExpirationMs);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString())
                .body(new LoginResponseDTO(loginData.name(), loginData.role()));
    }

    @Operation(summary = "Realizar Logout", description = "Invalida a sessão atual apagando os cookies do navegador e revogando o Refresh Token no banco de dados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank())
            service.revokeRefreshToken(refreshToken);

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
