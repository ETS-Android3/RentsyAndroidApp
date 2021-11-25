
package com.menlopark.rentsyuser.model.stores.Catalogs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalogList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

//    @SerializedName("data")
//    @Expose
//    private List<Detail> details = null;
//
//    public List<Detail> getDetails() {
//        return details;
//    }

//    public void setDetails(List<Detail> details) {
//        this.details = details;
//    }

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



}
