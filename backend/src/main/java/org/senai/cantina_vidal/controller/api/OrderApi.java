package org.senai.cantina_vidal.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.senai.cantina_vidal.dto.order.OrderCustomerResponseDTO;
import org.senai.cantina_vidal.dto.order.OrderRequestDTO;
import org.senai.cantina_vidal.dto.order.OrderResponseDTO;
import org.senai.cantina_vidal.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "3. Pedidos (Cliente)", description = "Área do cliente para realizar e acompanhar pedidos")
@SecurityRequirement(name = "cookieAuth")
public interface OrderApi {

    @Operation(summary = "Detalhes do Pedido", description = "Busca um pedido específico. O usuário só vê seus próprios pedidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado ou não pertence ao usuário")
    })
    ResponseEntity<OrderResponseDTO> findById(Long id, @Parameter(hidden = true) User user);

    @Operation(summary = "Meus Pedidos", description = "Lista o histórico de pedidos do usuário logado.")
    ResponseEntity<List<OrderResponseDTO>> findAllMyOrders(@Parameter(hidden = true) User user);

    @Operation(summary = "Realizar Pedido", description = "Cria um novo pedido validando estoque e disponibilidade.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido recebido e enviado para cozinha"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: Quantidade zerada)"),
            @ApiResponse(responseCode = "404", description = "Um ou mais produtos não foram encontrados"),
            @ApiResponse(responseCode = "409", description = "Produto sem estoque ou indisponível")
    })
    ResponseEntity<OrderCustomerResponseDTO> create(OrderRequestDTO dto, @Parameter(hidden = true) User user);
}
