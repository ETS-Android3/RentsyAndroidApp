package com.menlopark.rentsyuser.model.Forgot_model;

import com.google.gson.annotations.SerializedName;

public class ForgotParams {

    @SerializedName("email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
