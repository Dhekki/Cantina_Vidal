package com.example.projeto_v1.network;

import com.example.projeto_v1.model.Cliente;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface ApiService {


//    @Headers({
//            "Accept: application/json",
//            "Prefer: return=representation"
//    })
//    @GET("produtos")
//    Call<List<Produto>> listarProdutos(
//            @Header("apikey") String apiKey,
//            @Header("Authorization") String auth
//    );
//
//
//    @Headers({
//            "Content-Type: application/json",
//            "Prefer: return=representation"
//    })
//    @POST("produtos")
//    Call<Void> inserirProduto(
//            @Header("apikey") String apiKey,
//            @Header("Authorization") String auth,
//            @Body Produto produto
//    );
}
