package com.example.projeto_v1.repository;

import com.example.projeto_v1.model.Pedido;
import java.util.ArrayList;
import java.util.List;

public class PedidosRepository {

    private static PedidosRepository instance;
    private List<Pedido> historicoPedidos;

    private PedidosRepository() {
        historicoPedidos = new ArrayList<>();
    }

    public static PedidosRepository getInstance() {
        if (instance == null) {
            instance = new PedidosRepository();
        }
        return instance;
    }

    // Adiciona um NOVO pedido no topo da lista
    public void adicionarPedido(Pedido pedido) {
        if (historicoPedidos == null) {
            historicoPedidos = new ArrayList<>();
        }
        // Adiciona no índice 0 para aparecer primeiro (topo da tela)
        historicoPedidos.add(0, pedido);
    }

    public List<Pedido> getListaPedidos() {
        return historicoPedidos;
    }

    public void cancelarPedido(Pedido pedido) {
        if (historicoPedidos != null) {
            historicoPedidos.remove(pedido);
        }
    }

    public void limparHistorico() {
        if (historicoPedidos != null) {
            historicoPedidos.clear();
        }
    }
}