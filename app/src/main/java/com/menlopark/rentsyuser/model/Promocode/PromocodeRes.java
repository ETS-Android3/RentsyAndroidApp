package com.menlopark.rentsyuser.model.Promocode;

public class PromocodeRes {
    private PromocodeResData data;
    private String message;
    private int status;

    public PromocodeResData getData() {
        return this.data;
    }

    public void setData(PromocodeResData data) {
        this.data = data;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
