
package com.example.projeto_v1.model;

public class Produto {
    private Integer id;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidade;
    private Categoria categoria;

    private int imagemResourceId;

    public Produto(Integer id, String nome, String descricao, double preco, int imagemResourceId, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.imagemResourceId = imagemResourceId;
        this.quantidade = 0;
        this.categoria = categoria;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}