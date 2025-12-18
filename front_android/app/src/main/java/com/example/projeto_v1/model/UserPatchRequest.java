package com.example.projeto_v1.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserPatchRequest implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("imageUrl")
    private String imageUrl;

    public UserPatchRequest(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}