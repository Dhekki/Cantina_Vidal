package com.example.projeto_v1.model;

public class Categoria{
    private Integer id;
    private String nome;
    private int iconeResourceId;

    public Categoria(Integer id, String nome, int iconeResourceId) {
        this.id = id;
        this.nome = nome;
        this.iconeResourceId = iconeResourceId;
    }

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

    public int getIconeResourceId() {
        return iconeResourceId;
    }

    public void setIconeResourceId(int iconeResourceId) {
        this.iconeResourceId = iconeResourceId;
    }
}