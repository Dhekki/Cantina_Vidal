package com.example.projeto_v1.model;

public class Categoria{
    private Integer id;
    private String nome;
    private String descricao;
    private int iconeResourceId;

    public Categoria(Integer id, String nome, String descricao, int iconeResourceId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.iconeResourceId = iconeResourceId;
    }

//    public Categoria(Integer id, String nome, String descricao, int imagemResourceId) {
//        this(id, nome, descricao, imagemResourceId);
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIconeResourceId() {
        return iconeResourceId;
    }

    public void setIconeResourceId(int iconeResourceId) {
        this.iconeResourceId = iconeResourceId;
    }
}