package org.senai.cantina_vidal.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.senai.cantina_vidal.dto.user.UserPatchRequestDTO;
import org.senai.cantina_vidal.dto.user.UserResponseDTO;
import org.senai.cantina_vidal.entity.User;
import org.springframework.http.ResponseEntity;

@Tag(name = "8. Usuários", description = "Gestão de perfil e consulta de usuários")
@SecurityRequirement(name = "cookieAuth")
public interface UserApi {

    @Operation(summary = "Buscar por ID", description = "Apenas ADMIN pode buscar qualquer usuário. Usuários comuns só podem buscar o próprio ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este ID"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    ResponseEntity<UserResponseDTO> findById(Long id);

    @Operation(summary = "Meu Perfil", description = "Retorna os dados do usuário logado.")
    ResponseEntity<UserResponseDTO> me(@Parameter(hidden = true) User user);

    @Operation(summary = "Atualizar Perfil", description = "Atualiza dados cadastrais do usuário logado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<UserResponseDTO> meUpdate(
            @Parameter(hidden = true) User user, 
            UserPatchRequestDTO dto);

    @Operation(summary = "Excluir Conta", description = "Realiza a desativação (soft delete) da conta do usuário logado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conta desativada com sucesso")
    })
    ResponseEntity<Void> meDelete(@Parameter(hidden = true) User user);
}
