package com.example.projeto_v1.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Classe genérica para receber qualquer paginação do Spring Boot
public class PageResponse<T> {

    @SerializedName("content")
    private List<T> content;

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("totalElements")
    private long totalElements;

    @SerializedName("size")
    private int size;

    @SerializedName("number")
    private int number; // Página atual

    // Getters e Setters
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
}