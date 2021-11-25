package com.menlopark.rentsyuser.model.stores;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllCategory {

    @SerializedName("id")
    @Expose
    public Integer categoryId;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @SerializedName("name")
    @Expose
    public String categoryName;

}