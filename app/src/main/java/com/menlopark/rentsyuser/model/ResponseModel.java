package com.menlopark.rentsyuser.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseModel {

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("error")
    public ArrayList<String> error;

    public ArrayList<String> getError() {
        return error;
    }

    public void setError(ArrayList<String> error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//        private String status;
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    private String message;
//
//


}
