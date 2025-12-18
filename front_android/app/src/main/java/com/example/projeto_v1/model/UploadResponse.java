package com.example.projeto_v1.model;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class UploadResponse implements Serializable {



    @SerializedName("originalFilename")

    private String originalFilename;



    @SerializedName("fileDownloadUri")

    private String relativePath;



    @SerializedName("contentType")

    private String contentType;



    @SerializedName("size")

    private Long size;



    public UploadResponse() {

    }



    public String getOriginalFilename() {

        return originalFilename;

    }



    public void setOriginalFilename(String originalFilename) {

        this.originalFilename = originalFilename;

    }



    public String getRelativePath() {

        return relativePath;

    }



    public void setRelativePath(String relativePath) {

        this.relativePath = relativePath;

    }



    public String getContentType() {

        return contentType;

    }



    public void setContentType(String contentType) {

        this.contentType = contentType;

    }



    public Long getSize() {

        return size;

    }



    public void setSize(Long size) {

        this.size = size;

    }

}