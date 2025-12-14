package org.senai.cantina_vidal.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.senai.cantina_vidal.dto.order.OrderItemRequestDTO;
import org.senai.cantina_vidal.dto.order.OrderRequestDTO;
import org.senai.cantina_vidal.entity.Order;
import org.senai.cantina_vidal.entity.OrderItem;
import org.senai.cantina_vidal.entity.Product;
import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.enums.OrderStatus;
import org.senai.cantina_vidal.exception.ConflictException;
import org.senai.cantina_vidal.exception.ResourceNotFoundException;
import org.senai.cantina_vidal.repository.OrderItemRepository;
import org.senai.cantina_vidal.repository.OrderRepository;
import org.senai.cantina_vidal.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public List<Order> findUserOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
    }

    public Order findById(Long id, User user) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        if (!order.getUser().getId().equals(user.getId()) && !user.getRole().name().equals("ADMIN"))
            throw new ResourceNotFoundException("Pedido não encontrado");

        return order;
    }

    public List<Order> findAllOrders(OrderStatus status) {
        if (status != null)
            return orderRepository.findByStatusOrderByCreatedAtDesc(status.name());

        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Order updateStatus(Long id, OrderStatus requestStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));

        OrderStatus currentStatus = OrderStatus.valueOf(order.getStatus());

        if (currentStatus == OrderStatus.DELIVERED)
            throw new ConflictException("Pedidos finalizados não pode ser cancelado");

        if (requestStatus == OrderStatus.CANCELLED)
            order.setStatus(requestStatus.name());
        else
            order.setStatus(currentStatus.next().name());

        return orderRepository.save(order);
    }

    @Transactional
    public Order createOrder(OrderRequestDTO dto, User user) {
        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.RECEIVED.name())
                .total(BigDecimal.ZERO)
                .build();

        order = orderRepository.save(order);

        BigDecimal totalOrder = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequestDTO itemDto : dto.items()) {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + itemDto.productId()));

            if (!product.getActive())
                throw new ConflictException("Produto indisponível/inativo: " + product.getName());

            int availableStock = product.getQuantityStock() - product.getQuantityHeld() - product.getQuantityCommitted();

            if (availableStock < itemDto.quantity())
                throw new ConflictException("Estoque insuficiente para o produto: " + product.getName()
                        + ". Disponível: " + availableStock);

            product.setQuantityCommitted(product.getQuantityCommitted() + itemDto.quantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemDto.quantity())
                    .frozenUnitPrice(product.getCurrentPrice())
                    .build();

            orderItemRepository.save(orderItem);
            items.add(orderItem);

            BigDecimal itemTotal = product.getCurrentPrice().multiply(BigDecimal.valueOf(itemDto.quantity()));
            totalOrder = totalOrder.add(itemTotal);
        }

        order.setTotal(totalOrder);
        order.setItems(items);

        return orderRepository.save(order);
    }
}
