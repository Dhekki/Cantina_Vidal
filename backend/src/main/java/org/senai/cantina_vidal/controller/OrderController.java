package org.senai.cantina_vidal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.entity.Order;
import org.senai.cantina_vidal.service.OrderService;
import org.senai.cantina_vidal.dto.order.OrderRequestDTO;
import org.senai.cantina_vidal.dto.order.OrderResponseDTO;
import org.senai.cantina_vidal.dto.order.OrderCustomerResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "3. Pedidos (Cliente)", description = "Área do cliente para realizar e acompanhar pedidos")
@SecurityRequirement(name = "bearer-key")
public class OrderController {
    private final OrderService service;

    @Operation(summary = "Detalhes do Pedido", description = "Busca um pedido específico. O usuário só vê seus próprios pedidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado ou não pertence ao usuário")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new OrderResponseDTO(service.findById(id, user)));
    }

    @Operation(summary = "Meus Pedidos", description = "Lista o histórico de pedidos do usuário logado.")
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findAllMyOrders(@AuthenticationPrincipal User user) {
        List<Order> orders = service.findUserOrders(user);
        return ResponseEntity.ok(orders.stream().map(OrderResponseDTO::new).toList());
    }

    @Operation(summary = "Realizar Pedido", description = "Cria um novo pedido validando estoque e disponibilidade.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido recebido e enviado para cozinha"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: Quantidade zerada)"),
            @ApiResponse(responseCode = "404", description = "Um ou mais produtos não foram encontrados"),
            @ApiResponse(responseCode = "409", description = "Produto sem estoque ou indisponível")
    })
    @PostMapping
    public ResponseEntity<OrderCustomerResponseDTO> create(
            @RequestBody @Valid OrderRequestDTO dto,
            @AuthenticationPrincipal User user)
    {
        Order savedOrder = service.createOrder(dto, user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedOrder.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new OrderCustomerResponseDTO(savedOrder));
    }
}
