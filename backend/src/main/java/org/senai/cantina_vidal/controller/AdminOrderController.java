package org.senai.cantina_vidal.controller;

import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.controller.api.AdminOrderApi;
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
public class AdminOrderController implements AdminOrderApi {
    
    private final OrderService service;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new OrderResponseDTO(service.findById(id)));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findAll(
            @RequestParam(required = false) OrderStatus status) {
        
        List<Order> orders = service.findAllOrders(status);
        return ResponseEntity.ok(orders.stream()
                .map(OrderResponseDTO::new)
                .toList());
    }

    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody(required = false) OrderStatusDTO dto) {
        
        OrderStatus status = dto != null ? dto.status() : null;
        Order updateOrder = service.updateStatus(id, status);

        return ResponseEntity.ok(new OrderResponseDTO(updateOrder));
    }
}
