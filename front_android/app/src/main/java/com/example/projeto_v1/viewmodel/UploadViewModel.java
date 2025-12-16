package com.example.projeto_v1.viewmodel;



import android.content.Context;

import android.net.Uri;



import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;



import com.example.projeto_v1.model.UploadResponse;

import com.example.projeto_v1.repository.UploadRepository;

import com.example.projeto_v1.utils.FileUtils;



import java.io.File;



public class UploadViewModel extends ViewModel {



    private final UploadRepository repository;



    public UploadViewModel() {

        this.repository = new UploadRepository();

    }



    // Recebe a URI do Android e o Token, faz a mágica e retorna o resultado

    public LiveData<UploadResponse> fazerUploadImagem(Context context, Uri imageUri, String token) {

        // 1. Converter URI para File

        File file = FileUtils.getFileFromUri(context, imageUri);



        if (file != null) {

            // 2. Chamar repositório

            return repository.uploadImage(token, file);

        } else {

            // Erro ao processar arquivo localmente

            return new MutableLiveData<>(null);

        }

    }

}