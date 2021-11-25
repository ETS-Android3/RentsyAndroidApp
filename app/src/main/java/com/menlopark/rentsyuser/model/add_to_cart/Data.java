
package com.menlopark.rentsyuser.model.add_to_cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("cart_info")
    @Expose
    private List<CartInfo> cartInfo = null;

    public List<CartInfo> getCartInfo() {
        return cartInfo;
    }

    public void setCartInfo(List<CartInfo> cartInfo) {
        this.cartInfo = cartInfo;
    }

}