package com.menlopark.rentsyuser.model.Setting.Post_Setting;

public class PostSettingParam {

    String location_id;
    String customer_id;
    String radious;
    String current_location;
    String push_notification_status;
    String email_alerts_status;
    String email_invoice_status;

    public String getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }



    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getRadious() {
        return radious;
    }

    public void setRadious(String radious) {
        this.radious = radious;
    }



    public String getPush_notification_status() {
        return push_notification_status;
    }

    public void setPush_notification_status(String push_notification_status) {
        this.push_notification_status = push_notification_status;
    }

    public String getEmail_alerts_status() {
        return email_alerts_status;
    }

    public void setEmail_alerts_status(String email_alerts_status) {
        this.email_alerts_status = email_alerts_status;
    }

    public String getEmail_invoice_status() {
        return email_invoice_status;
    }

    public void setEmail_invoice_status(String email_invoice_status) {
        this.email_invoice_status = email_invoice_status;
    }


}
