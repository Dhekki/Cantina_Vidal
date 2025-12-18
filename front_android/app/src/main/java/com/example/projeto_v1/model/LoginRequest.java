package com.example.projeto_v1.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class LoginRequest implements Serializable {


    @SerializedName("email")

    private String email;


    @SerializedName("password")

    private String password;


    public LoginRequest(String email, String password) {

        this.email = email;

        this.password = password;

    }


// Getters e Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}