package com.example.projeto_v1.model;

public class Produto {
    private Integer id;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidade;
    private int imagemResourceId;

    // Construtor completo
    public Produto(Integer id, String nome, String descricao, double preco, int imagemResourceId, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.imagemResourceId = imagemResourceId;
        this.quantidade = quantidade;
    }

    public Produto(Integer id, String nome, String descricao, double preco,int imagemResourceId) {
        this(id, nome, descricao, preco, imagemResourceId, 0);
    }

    public Integer getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public double getPreco() { return preco; }
    public int getImagemResourceId() { return imagemResourceId; }
    public int getQuantidade() { return quantidade; }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}