package org.senai.cantina_vidal.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.senai.cantina_vidal.dto.order.OrderResponseDTO;
import org.senai.cantina_vidal.dto.order.OrderStatusDTO;
import org.senai.cantina_vidal.enums.OrderStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "4. Pedidos (Admin)", description = "Gestão de fila e status pela cozinha")
@SecurityRequirement(name = "cookieAuth")
public interface AdminOrderApi {

    @Operation(summary = "Buscar Pedido", description = "Visualiza qualquer pedido do sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    ResponseEntity<OrderResponseDTO> findById(Long id);

    @Operation(summary = "Fila de Pedidos", description = "Lista pedidos, opcionalmente filtrando por status.")
    ResponseEntity<List<OrderResponseDTO>> findAll(
            @Parameter(description = "Filtra por status (ex: RECEIVED, IN_PREPARATION)") OrderStatus status);

    @Operation(summary = "Atualizar Status", description = """
            Avança o status do pedido. Comportamento depende do corpo:
            * **Sem corpo (Vazio):** Avança para a próxima etapa lógica (Recebido -> Preparando -> Pronto -> Entregue).
            * **Com corpo:** Força um status específico (Útil para **CANCELLED** ou **NOT_DELIVERED**).
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado"),
            @ApiResponse(responseCode = "400", description = "Status inválido enviado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "409", description = "Transição de status não permitida (Ex: Cancelado -> Pronto)")
    })
    ResponseEntity<OrderResponseDTO> updateStatus(Long id, OrderStatusDTO dto);
}
