package com.example.projeto_v1.repository;

import com.example.projeto_v1.model.Produto;
import java.util.ArrayList;
import java.util.List;

public class PedidosRepository {
    private static final PedidosRepository instance = new PedidosRepository();
    private final List<Produto> pedidoAtual = new ArrayList<>();
    private boolean temPedidoAtivo = false;

    private PedidosRepository() {}

    public static PedidosRepository getInstance() {
        return instance;
    }

    public void confirmarPedido(List<Produto> itensDoCarrinho) {
        pedidoAtual.clear();
        // Clona a lista para que alterações futuras no carrinho não afetem o pedido feito
        pedidoAtual.addAll(itensDoCarrinho);
        temPedidoAtivo = true;
    }

    public List<Produto> getPedidoAtual() {
        return pedidoAtual;
    }

    public void cancelarPedido() {
        pedidoAtual.clear();
        temPedidoAtivo = false;
    }

    public boolean hasPedidoAtivo() {
        return temPedidoAtivo;
    }

    public double getTotalPedido() {
        double total = 0;
        for (Produto p : pedidoAtual) {
            total += (p.getPreco() * p.getQuantidade());
        }
        return total;
    }
}