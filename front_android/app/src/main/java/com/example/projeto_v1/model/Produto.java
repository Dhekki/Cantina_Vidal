package com.example.projeto_v1.model;

import java.io.Serializable;
import java.util.UUID;

public class Produto implements Serializable {
    // Alterado para Long para ser compatível com a API, mas mantendo compatibilidade com UUID se necessário convertendo
    private Long id;
    private String nome;
    private String descricao;
    private double preco;

    // Novos campos para imagens
    private String imageUrl; // URL vinda da API
    private int imagemResourceId; // Imagem padrão local (fallback)

    private int quantidade;
    private Categoria categoria;

    // Construtor vazio (Necessário para o Retrofit/Gson)
    public Produto() {
    }

    // Construtor Completo (Usado para criar produtos manualmente/mock)
    public Produto(String nome, String descricao, double preco, int imagemResourceId, Categoria categoria) {
        // this.id = ...; // O ID geralmente vem do banco, não geramos mais UUID aqui se for usar API
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.imagemResourceId = imagemResourceId;
        this.categoria = categoria;
        this.quantidade = 0; // Na vitrine começa com 0
    }

    // Construtor de Cópia
    public Produto(Produto outroProduto) {
        this.id = outroProduto.id;
        this.nome = outroProduto.nome;
        this.descricao = outroProduto.descricao;
        this.preco = outroProduto.preco;
        this.imageUrl = outroProduto.imageUrl;
        this.imagemResourceId = outroProduto.imagemResourceId;
        this.categoria = outroProduto.categoria;
        this.quantidade = outroProduto.quantidade;
    }

    // --- GETTERS E SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getImagemResourceId() { return imagemResourceId; }
    public void setImagemResourceId(int imagemResourceId) { this.imagemResourceId = imagemResourceId; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}