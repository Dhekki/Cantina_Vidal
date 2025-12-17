package com.example.projeto_v1.network;

import com.example.projeto_v1.model.Cliente;
import com.example.projeto_v1.model.UploadResponse;
import com.example.projeto_v1.model.UserResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @Multipart
    @POST("uploads")
    Call<UploadResponse> uploadImage(@Header("Authorization") String token,
                                     @Part MultipartBody.Part file);

    @GET("users/me")
    Call<UserResponse> getMyProfile(@Header("Authorization") String token);
}
