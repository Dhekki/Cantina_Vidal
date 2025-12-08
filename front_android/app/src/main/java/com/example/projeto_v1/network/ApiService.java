package com.example.projeto_v1.network;

import com.example.projeto_v1.model.Cliente;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiService {


    @Headers({
            "Accept: application/json",
            "Prefer: return=representation"
    })
    @GET("clientes")
    Call<List<Cliente>> listarClientes(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth
    );


    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @POST("clientes")
    Call<Void> cadastrarCliente(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth,
            @Body Cliente cliente
    );

    @Headers({
            "Content-Type: application/json"
    })
    @GET("clientes")
    Call<List<Cliente>> realizarLogin(
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth,
            @Query("email") String email,
            @Query("senha") String senha
    );
}
