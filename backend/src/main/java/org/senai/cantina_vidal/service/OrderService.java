package org.senai.cantina_vidal.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.senai.cantina_vidal.dto.order.OrderItemRequestDTO;
import org.senai.cantina_vidal.dto.order.OrderRequestDTO;
import org.senai.cantina_vidal.dto.order.OrderResponseDTO;
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
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final SseService sseService;
    private final Random random = new Random();

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
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));

        OrderStatus currentStatus = order.getStatus();

        if (currentStatus.isTerminal())
            throw new ConflictException("Pedidos finalizados não podem ser alterados");

        if (requestStatus == OrderStatus.NOT_DELIVERED) {
           if (currentStatus != OrderStatus.DONE)
               throw new ConflictException("Apenas pedidos prontos podem ser marcados como não retirados");

            order.setStatus(requestStatus);
        } else if (requestStatus == OrderStatus.CANCELLED)
            order.setStatus(requestStatus);
        else
            order.setStatus(currentStatus.next());

        return orderRepository.save(order);
    }

    @Transactional
    public Order createOrder(OrderRequestDTO dto, User user) {
        Order order = initializeOrder(user);

        List<Product> products = fetchAndValidateProducts(dto.items());

        processOrderItems(order, dto.items(), products);

        return saveAndNotify(order);
    }

    private int getDailyId() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        return orderRepository.findMaxDailyIdOfDay(startOfDay, endOfDay)
                .orElse(0) + 1;
    }

    private String getPickupCode() {
        StringBuilder sb = new StringBuilder();
        String characters = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";
        int codeLength = 3;

        for (int i = 0; i < codeLength; i++)
            sb.append(characters.charAt(random.nextInt(characters.length())));

        return sb.toString();
    }

    private Order initializeOrder(User user) {
        return orderRepository.save(Order.builder()
                .user(user)
                .dailyId(getDailyId())
                .pickupCode(getPickupCode())
                .status(OrderStatus.RECEIVED)
                .total(BigDecimal.ZERO)
                .build());
    }

    private List<Product> fetchAndValidateProducts(List<OrderItemRequestDTO> itemsDto) {
        Set<Long> itemsId = itemsDto.stream()
                .map(OrderItemRequestDTO::productId)
                .collect(Collectors.toSet());

        List<Product> products = productService.findAllById(itemsId);

        if (products.size() != itemsId.size())
            throw new ResourceNotFoundException("Um ou mais produtos não foram encontrados no banco de dados.");

        return products;
    }

    private void validateStockAndAvailability(Product product, Integer quantity) {
        if (!product.getAvailable())
            throw new ConflictException("Produto indisponível no momento: " + product.getName());

        int availableStock = product.getQuantityStock() - product.getQuantityHeld() - product.getQuantityCommitted();

        if (availableStock < quantity)
            throw new ConflictException("Estoque insuficiente para o produto: " + product.getName()
                    + ". Disponível: " + availableStock);
    }

    private void updateProductStock(Product product, Integer quantity) {
        product.setQuantityCommitted(product.getQuantityCommitted() + quantity);
    }

    private OrderItem buildOrderItem(Order order, Product product, Integer quantity) {
        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .frozenUnitPrice(product.getCurrentPrice())
                .build();
    }

    private void processOrderItems(Order order, List<OrderItemRequestDTO> itemsDto, List<Product> products) {
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        BigDecimal totalOrder = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDto : itemsDto) {
            Product product = productMap.get(itemDto.productId());

            validateStockAndAvailability(product, itemDto.quantity());
            updateProductStock(product, itemDto.quantity());

            order.addItem(product, itemDto.quantity());
        }

        order.calculateTotal();

        productRepository.saveAll(products);
    }

    private Order saveAndNotify(Order order) {
        Order savedOrder = orderRepository.save(order);

        OrderResponseDTO responseDTO = new OrderResponseDTO(savedOrder);
        sseService.notifyKitchenNewOrder(responseDTO);

        for (OrderItem item : savedOrder.getItems()) {
            Product p = item.getProduct();
            sseService.notifyProductUpdate(p.getId(), p.getRealStock(), p.getAvailable());
        }

        return savedOrder;
    }
}
