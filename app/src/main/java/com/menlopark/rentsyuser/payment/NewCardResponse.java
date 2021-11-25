package com.menlopark.rentsyuser.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewCardResponse {

    @Expose
    @SerializedName("code")
    private int code;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("url")
    private String url;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }
}