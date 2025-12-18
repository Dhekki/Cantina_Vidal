package com.example.projeto_v1.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class LoginResponse implements Serializable {


    @SerializedName("accessToken")

    private String accessToken;


    @SerializedName("refreshToken")

    private String refreshToken;


    @SerializedName("name")

    private String name;


    @SerializedName("role")

    private String role;


    public LoginResponse() {
    }


// Getters e Setters

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}