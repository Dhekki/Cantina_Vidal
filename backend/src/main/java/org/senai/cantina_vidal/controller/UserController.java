package org.senai.cantina_vidal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.service.UserService;
import org.senai.cantina_vidal.dto.user.UserResponseDTO;
import org.senai.cantina_vidal.dto.user.UserPatchRequestDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "8. Usuários", description = "Gestão de perfil e consulta de usuários")
@SecurityRequirement(name = "bearer-key")
public class UserController {
    private final UserService service;

    @Operation(summary = "Buscar por ID", description = "Apenas ADMIN pode buscar qualquer usuário. Usuários comuns só podem buscar o próprio ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este ID"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new UserResponseDTO(service.findById(id)));
    }

    @Operation(summary = "Meu Perfil", description = "Retorna os dados do usuário logado.")
    @GetMapping("/me")
    ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    @Operation(summary = "Atualizar Perfil", description = "Atualiza dados cadastrais do usuário logado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PatchMapping("/me")
    ResponseEntity<UserResponseDTO> meUpdate(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid UserPatchRequestDTO dto) {
        return ResponseEntity.ok(new UserResponseDTO(service.update(user.getId(), dto)));
    }

    @Operation(summary = "Excluir Conta", description = "Realiza a desativação (soft delete) da conta do usuário logado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conta desativada com sucesso")
    })
    @DeleteMapping("/me")
    ResponseEntity<Void> meDelete(@AuthenticationPrincipal User user) {
        service.delete(user.getId());
        return ResponseEntity.noContent().build();
    }
}
