package com.example.projeto_v1.network;

import com.example.projeto_v1.model.CategoryResponse;
import com.example.projeto_v1.model.LoginRequest;
import com.example.projeto_v1.model.LoginResponse;
import com.example.projeto_v1.model.OrderCustomerResponse;
import com.example.projeto_v1.model.OrderRequest;
import com.example.projeto_v1.model.OrderResponse;
import com.example.projeto_v1.model.PageResponse;
import com.example.projeto_v1.model.ProductCustomerResponse;
import com.example.projeto_v1.model.RegisterRequest;
import com.example.projeto_v1.model.UploadResponse;
import com.example.projeto_v1.model.UserPatchRequest;
import com.example.projeto_v1.model.UserResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {

    @Multipart
    @POST("uploads")
    Call<UploadResponse> uploadImage(@Header("Authorization") String token,
                                     @Part MultipartBody.Part file);

    @GET("users/me")
    Call<UserResponse> getMyProfile(@Header("Authorization") String token);

    @PATCH("users/me")
    Call<UserResponse> updateMyProfile(
            @Header("Authorization") String token,
            @Body UserPatchRequest body
    );

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);


    @POST("auth/register")
    Call<UserResponse> register(@Body RegisterRequest registerRequest);

    @GET("products")
    Call<PageResponse<ProductCustomerResponse>> getProducts(
            @Header("Authorization") String token,
            @Query("name") String name,
            @Query("categoryId") Long categoryId,
            @Query("page") int page,
            @Query("size") int size,
            @Query("sort") String sort
    );

    @GET("products/{id}")
    Call<ProductCustomerResponse> getProductById(
            @Header("Authorization") String token,
            @Path("id") Long id
    );

    @GET("categories")
    Call<PageResponse<CategoryResponse>> getCategories(
            @Query("page") int page,
            @Query("size") int size,
            @Query("sort") String sort
    );

    @GET("categories/{id}")
    Call<CategoryResponse> getCategoryById(
            @Path("id") Long id
    );

    @GET("orders")
    Call<List<OrderResponse>> getMyOrders(
            @Header("Authorization") String token
    );

    // Detalhes de um pedido
    @GET("orders/{id}")
    Call<OrderResponse> getOrderById(
            @Header("Authorization") String token,
            @Path("id") Long id
    );

    // Criar novo pedido
    @POST("orders")
    Call<OrderCustomerResponse> createOrder(
            @Header("Authorization") String token,
            @Body OrderRequest request
    );
}
