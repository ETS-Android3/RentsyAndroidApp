package com.menlopark.rentsyuser.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewCard {

    @Expose
    @SerializedName("customer_id")
    public int customerId;

    @Expose
    @SerializedName("token")
    public String token;

    public int getCustomerId() {
        return customerId;
    }

    public String getToken() {
        return token;
    }


    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "NewCard{" +
                "customerId=" + customerId +
                ", token='" + token + '\'' +
                '}';
    }
}