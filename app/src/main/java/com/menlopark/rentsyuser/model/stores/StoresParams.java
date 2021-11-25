package com.menlopark.rentsyuser.model.stores;

public class StoresParams {
    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    private String location_id;

    public String getBusiness_category_id() {
        return business_category_id;
    }

    public void setBusiness_category_id(String business_category_id) {
        this.business_category_id = business_category_id;
    }

    private String business_category_id;

    public String getBusiness_subcategory_id() {
        return business_subcategory_id;
    }

    public void setBusiness_subcategory_id(String business_subcategory_id) {
        this.business_subcategory_id = business_subcategory_id;
    }

    private String business_subcategory_id;

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getIs_min_max() {
        return is_min_max;
    }

    public void setIs_min_max(String is_min_max) {
        this.is_min_max = is_min_max;
    }

    private String min_price;
    private String max_price;
    private String is_min_max;
}
