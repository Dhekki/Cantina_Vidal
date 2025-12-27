package org.senai.cantina_vidal.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.dto.order.OrderResponseDTO;
import org.senai.cantina_vidal.dto.order.OrderStatusDTO;
import org.senai.cantina_vidal.entity.Order;
import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.enums.OrderStatus;
import org.senai.cantina_vidal.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
@SecurityRequirement(name = "bearer-key")
public class AdminOrderController {
    private final OrderService service;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new OrderResponseDTO(service.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findAll(@RequestParam(required = false) OrderStatus status) {
        List<Order> orders = service.findAllOrders(status);
        return ResponseEntity.ok(orders.stream()
                .map(OrderResponseDTO::new)
                .toList());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody(required = false) OrderStatusDTO dto) {
        OrderStatus status = dto != null ? dto.status() : null;

        Order updateOrder = service.updateStatus(id, status);

        return ResponseEntity.ok(new OrderResponseDTO(updateOrder));
    }
}
