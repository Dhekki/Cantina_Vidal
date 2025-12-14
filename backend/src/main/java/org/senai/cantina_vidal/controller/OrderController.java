package org.senai.cantina_vidal.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.dto.order.OrderRequestDTO;
import org.senai.cantina_vidal.dto.order.OrderResponseDTO;
import org.senai.cantina_vidal.dto.order.OrderStatusDTO;
import org.senai.cantina_vidal.entity.Order;
import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.enums.OrderStatus;
import org.senai.cantina_vidal.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@SecurityRequirement(name = "bearer-key")
public class OrderController {
    private final OrderService service;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new OrderResponseDTO(service.findById(id, user)));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findAllMyOrders(@AuthenticationPrincipal User user) {
        List<Order> orders = service.findUserOrders(user);
        return ResponseEntity.ok(orders.stream().map(OrderResponseDTO::new).toList());
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(
            @RequestBody @Valid OrderRequestDTO dto,
            @AuthenticationPrincipal User user)
    {
        Order savedOrder = service.createOrder(dto, user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedOrder.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new OrderResponseDTO(savedOrder));
    }
}
