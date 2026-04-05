package org.senai.cantina_vidal.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.senai.cantina_vidal.dto.auth.LoginRequestDTO;
import org.senai.cantina_vidal.dto.auth.LoginResponseDTO;
import org.senai.cantina_vidal.dto.auth.RegisterRequestDTO;
import org.senai.cantina_vidal.dto.user.UserResponseDTO;
import org.springframework.http.ResponseEntity;

@Tag(name = "1. Autenticação", description = "Endpoints para login/cadastro e gestão de tokens")
public interface AuthApi {

    @Operation(summary = "Realizar Login", description = "Autentica um usuário e retorna tokens de acesso e refresh.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de login inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciais incorretas")
    })
    ResponseEntity<LoginResponseDTO> login(LoginRequestDTO data);

    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de cliente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (Senha curta, email malformado)"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    ResponseEntity<UserResponseDTO> register(RegisterRequestDTO data);

    @Operation(summary = "Atualizar Token (Refresh)", description = "Gera um novo Access Token usando um Refresh Token válido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token renovado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Formato do token inválido"),
            @ApiResponse(responseCode = "401", description = "Refresh Token expirado (Faça login novamente)"),
            @ApiResponse(responseCode = "404", description = "Refresh Token não encontrado no banco (Revogado ou inexistente)")
    })
    ResponseEntity<LoginResponseDTO> refreshToken(
            @Parameter(hidden = true) String refreshToken);

    @Operation(summary = "Realizar Logout", description = "Invalida a sessão atual apagando os cookies do navegador e revogando o Refresh Token no banco de dados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso")
    })
    ResponseEntity<Void> logout(
            @Parameter(hidden = true) String refreshToken);
}
