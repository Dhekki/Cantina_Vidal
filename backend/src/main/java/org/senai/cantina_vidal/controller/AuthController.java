package org.senai.cantina_vidal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.senai.cantina_vidal.dto.auth.*;
import org.senai.cantina_vidal.service.AuthService;
import org.senai.cantina_vidal.dto.user.UserResponseDTO;

import org.senai.cantina_vidal.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "1. Autenticação", description = "Endpoints para login/cadastro e gestão de tokens")
public class AuthController {
    private final AuthService service;
    private final TokenService tokenService;

    @Operation(summary = "Realizar Login", description = "Autentica um usuário e retorna tokens de acesso e refresh.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de login inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciais incorretas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        return ResponseEntity.ok(service.login(data));
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
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO data) {
        return ResponseEntity.ok(service.refreshToken(data.refreshToken()));
    }

    @Operation(summary = "Validar Token", description = "Verifica se um Access Token ainda é válido (Check de Integridade/Expiração).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token Válido"),
            @ApiResponse(responseCode = "400", description = "Formato inválido"),
            @ApiResponse(responseCode = "401", description = "Token expirado ou assinatura inválida")
    })
    @PostMapping("/validate")
    public ResponseEntity<Void> validate(@RequestBody @Valid AccessTokenRequestDTO data) {
        tokenService.validateToken(data.accessToken());
        return ResponseEntity.ok().build();
    }
}
