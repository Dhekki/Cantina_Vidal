package com.example.projeto_v1.model;

import java.io.Serializable;

public class Categoria implements Serializable {
    private Long id; // Changed to Long to match API
    private String nome;
    private String imageUrl; // URL from API
    private String colorHex; // Color from API
    private int iconeResourceId; // Local fallback resource

    // Constructor for API data
    public Categoria(Long id, String nome, String imageUrl, String colorHex) {
        this.id = id;
        this.nome = nome;
        this.imageUrl = imageUrl;
        this.colorHex = colorHex;
        this.iconeResourceId = 0; // Default or specific placeholder
    }

    // Constructor for local/manual data (e.g., "Todos")
    public Categoria(Long id, String nome, int iconeResourceId) {
        this.id = id;
        this.nome = nome;
        this.iconeResourceId = iconeResourceId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public int getIconeResourceId() { return iconeResourceId; }
    public void setIconeResourceId(int iconeResourceId) { this.iconeResourceId = iconeResourceId; }
}