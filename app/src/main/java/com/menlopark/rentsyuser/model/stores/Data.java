
package com.menlopark.rentsyuser.model.stores;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public List<AllCategory> getAllCategory() {
        return allCategory;
    }

    public void setAllCategory(List<AllCategory> allCategory) {
        this.allCategory = allCategory;
    }

    @SerializedName("all_category")
    @Expose
    public List<AllCategory> allCategory = null;

    public List<com.menlopark.rentsyuser.model.stores.AllSubCategory> getAllSubCategory() {
        return AllSubCategory;
    }

    public void setAllSubCategory(List<com.menlopark.rentsyuser.model.stores.AllSubCategory> allSubCategory) {
        AllSubCategory = allSubCategory;
    }

    @SerializedName("all_subcategory")
    @Expose
    public List<AllSubCategory> AllSubCategory = null;

    public List<com.menlopark.rentsyuser.model.stores.AllPrice> getAllPrice() {
        return AllPrice;
    }

    public void setAllPrice(List<com.menlopark.rentsyuser.model.stores.AllPrice> allPrice) {
        AllPrice = allPrice;
    }

    public List<AllPrice> AllPrice = null;

}
