package com.example.projeto_v1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.projeto_v1.model.CategoryResponse;
import com.example.projeto_v1.repository.CategoriaRepository;

import java.util.List;

public class CategoriaViewModel extends ViewModel {

    private final CategoriaRepository repository;

    public CategoriaViewModel() {
        this.repository = new CategoriaRepository();
    }

    public LiveData<List<CategoryResponse>> listarCategorias() {
        return repository.getCategories();
    }
}