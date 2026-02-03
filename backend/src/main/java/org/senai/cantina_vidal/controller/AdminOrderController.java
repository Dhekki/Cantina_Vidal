package org.senai.cantina_vidal.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.senai.cantina_vidal.entity.Order;
import org.senai.cantina_vidal.enums.OrderStatus;
import org.senai.cantina_vidal.service.OrderService;
import org.senai.cantina_vidal.dto.order.OrderStatusDTO;
import org.senai.cantina_vidal.dto.order.OrderResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
@Tag(name = "4. Pedidos (Admin)", description = "Gestão de fila e status pela cozinha")
@SecurityRequirement(name = "bearer-key")
public class AdminOrderController {
    private final OrderService service;

    @Operation(summary = "Buscar Pedido", description = "Visualiza qualquer pedido do sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new OrderResponseDTO(service.findById(id)));
    }

    @Operation(summary = "Fila de Pedidos", description = "Lista pedidos, opcionalmente filtrando por status.")
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findAll(
            @Parameter(description = "Filtra por status (ex: RECEIVED, IN_PREPARATION)")
            @RequestParam(required = false) OrderStatus status) {
        List<Order> orders = service.findAllOrders(status);
        return ResponseEntity.ok(orders.stream()
                .map(OrderResponseDTO::new)
                .toList());
    }

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
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody(required = false) OrderStatusDTO dto) {
        OrderStatus status = dto != null ? dto.status() : null;

        Order updateOrder = service.updateStatus(id, status);

        return ResponseEntity.ok(new OrderResponseDTO(updateOrder));
    }
}
