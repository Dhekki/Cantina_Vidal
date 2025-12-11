package com.example.projeto_v1.model;

public class Categoria {
    private int id;
    private String nome;
    private int iconeResId;
    private int corFundo;

    public Categoria(int id, String nome, int iconeResId, int corFundo) {
        this.id = id;
        this.nome = nome;
        this.iconeResId = iconeResId;
        this.corFundo = corFundo;
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getIconeResId() { return iconeResId; }
    public int getCorFundo() { return corFundo; }
}