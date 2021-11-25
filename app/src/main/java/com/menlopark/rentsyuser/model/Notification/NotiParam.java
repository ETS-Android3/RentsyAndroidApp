package com.menlopark.rentsyuser.model.Notification;

public class NotiParam {

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    private String customer_id;
    private String page;
}
