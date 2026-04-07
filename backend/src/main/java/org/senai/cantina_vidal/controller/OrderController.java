package org.senai.cantina_vidal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.controller.api.OrderApi;
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
public class OrderController implements OrderApi {
    private final OrderService service;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById(
            @PathVariable Long id, 
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new OrderResponseDTO(service.findById(id, user)));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findAllMyOrders(
            @AuthenticationPrincipal User user) {
        List<Order> orders = service.findUserOrders(user);
        return ResponseEntity.ok(orders.stream().map(OrderResponseDTO::new).toList());
    }

    @Override
    @PostMapping
    public ResponseEntity<OrderCustomerResponseDTO> create(
            @RequestBody @Valid OrderRequestDTO dto,
            @AuthenticationPrincipal User user) {
        
        Order savedOrder = service.createOrder(dto, user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedOrder.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new OrderCustomerResponseDTO(savedOrder));
    }
}
