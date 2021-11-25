package com.menlopark.rentsyuser.model.stores;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllPrice {


    public String minPrice;

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String maxPrice;
    public String itemName;

}