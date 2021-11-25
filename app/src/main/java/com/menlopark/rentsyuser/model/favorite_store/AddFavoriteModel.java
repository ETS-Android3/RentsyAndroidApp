package com.menlopark.rentsyuser.model.favorite_store;

public class AddFavoriteModel {
    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    String store_id;
    String customer_id;
    Integer is_favourite;

    public Integer getIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(Integer is_favourite) {
        this.is_favourite = is_favourite;
    }
}
