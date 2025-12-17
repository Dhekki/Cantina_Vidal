package com.example.projeto_v1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pedido {
    private String id;
    private List<Produto> produtos;
    private String status;
    private double valorTotal;

    public Pedido(List<Produto> produtosOriginais) {
        this.id = UUID.randomUUID().toString();
        this.status = "Em preparo";
        this.produtos = new ArrayList<>();

        // --- CORREÇÃO DO BUG ---
        // Em vez de adicionar o produto original, criamos um CLONE (new Produto(p))
        // Isso garante que mexer no carrinho depois NÃO altere esse pedido antigo.
        if (produtosOriginais != null) {
            for (Produto p : produtosOriginais) {
                this.produtos.add(new Produto(p));
            }
        }

        calcularTotal();
    }

    private void calcularTotal() {
        this.valorTotal = 0.0;
        if (produtos != null) {
            for (Produto p : produtos) {
                this.valorTotal += p.getPreco() * p.getQuantidade();
            }
        }
    }

    public String getId() { return id; }
    public List<Produto> getProdutos() { return produtos; }
    public String getStatus() { return status; }
    public double getValorTotal() { return valorTotal; }

    public int getQuantidadeItens() {
        int qtd = 0;
        if (produtos != null) {
            for (Produto p : produtos) qtd += p.getQuantidade();
        }
        return qtd;
    }
}