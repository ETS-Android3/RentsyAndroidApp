
package com.menlopark.rentsyuser.model.Facebook_Model;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("customer")
    private Customer mCustomer;

    public Customer getCustomer() {
        return mCustomer;
    }

    public void setCustomer(Customer customer) {
        mCustomer = customer;
    }

}
