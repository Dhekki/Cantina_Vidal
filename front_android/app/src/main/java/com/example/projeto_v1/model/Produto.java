package com.example.projeto_v1.model;

import java.io.Serializable;
import java.util.UUID;

public class Produto implements Serializable {
    private String id;
    private String nome;
    private String descricao;
    private double preco;
    private int imagemResourceId;
    private int quantidade;
    private Categoria categoria;

    // Construtor Padrão (Criação na Home)
    public Produto(String nome, String descricao, double preco, int imagemResourceId, Categoria categoria) {
        this.id = UUID.randomUUID().toString(); // ID Único Automático
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.imagemResourceId = imagemResourceId;
        this.categoria = categoria;
        this.quantidade = 1; // Padrão
    }

    // Construtor de Cópia (Deep Copy - Essencial para Pedidos)
    public Produto(Produto outroProduto) {
        this.id = outroProduto.id;
        this.nome = outroProduto.nome;
        this.descricao = outroProduto.descricao;
        this.preco = outroProduto.preco;
        this.imagemResourceId = outroProduto.imagemResourceId;
        this.categoria = outroProduto.categoria;

        // Copia a quantidade exata do momento da compra
        this.quantidade = outroProduto.quantidade;
    }

    // --- GETTERS E SETTERS ---

    public String getId() { return id; }

    public String getNome() { return nome; }

    public String getDescricao() { return descricao; }

    public double getPreco() { return preco; }

    public int getImagemResourceId() { return imagemResourceId; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}