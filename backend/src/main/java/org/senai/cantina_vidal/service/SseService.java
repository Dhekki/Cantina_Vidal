package org.senai.cantina_vidal.service;

import lombok.extern.slf4j.Slf4j;
import org.senai.cantina_vidal.dto.order.OrderResponseDTO;
import org.senai.cantina_vidal.dto.product.ProductStockUpdateDTO;
import org.senai.cantina_vidal.event.OrderCreatedEvent;
import org.senai.cantina_vidal.event.ProductUpdateEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class SseService {
    private final List<SseEmitter> kitchenEmitters = new CopyOnWriteArrayList<>();
    private final List<SseEmitter> catalogEmitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribeKitchen() {
        return createEmitter(kitchenEmitters, "Cozinha");
    }

    public SseEmitter subscribeCatalog() {
        return createEmitter(catalogEmitters, "Cliente/Catálogo");
    }

    private SseEmitter createEmitter(List<SseEmitter> list, String type) {
        SseEmitter emitter = new SseEmitter(3600000L);

        list.add(emitter);
        log.info("Nova conexão SSE [{}] estabelecida. Total: {}", type, list.size());

        Runnable removeCallback = () -> {
            list.remove(emitter);
            log.debug("Conexão SSE [{}] encerrada.", type);
        };

        emitter.onCompletion(removeCallback);
        emitter.onTimeout(removeCallback);
        emitter.onError((e) -> removeCallback.run());

        return emitter;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCreated(OrderCreatedEvent event) {
        sendEventToGroup(kitchenEmitters, "new-order", event.orderDTO());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductUpdate(ProductUpdateEvent event) {
        ProductStockUpdateDTO payload = new ProductStockUpdateDTO(event.productId(), event.newStock(), event.available());
        sendEventToGroup(catalogEmitters, "product-update", payload);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCatalogRefresh() {
        sendEventToGroup(catalogEmitters, "catalog-refresh", "reload");
    }

    private void sendEventToGroup(List<SseEmitter> emitters, String eventName, Object data) {
        if (emitters.isEmpty()) return;

        log.info("Enviando evento '{}' para {} clientes.", eventName, emitters.size());
        List<SseEmitter> failedEmitters = new CopyOnWriteArrayList<>();

        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
            } catch (IOException e) {
                failedEmitters.add(emitter);
            }
        });

        emitters.removeAll(failedEmitters);
    }
}
