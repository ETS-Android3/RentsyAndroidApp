
package com.menlopark.rentsyuser.model.add_to_cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartInfo {

    @SerializedName("cart_id")
    @Expose
    private Integer cartId;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("images")
    @Expose
    private String images;
    @SerializedName("cataloguename")
    @Expose
    private String cataloguename;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("length")
    @Expose
    private Integer length;
    @SerializedName("booking_period")
    @Expose
    private String bookingPeriod;
    @SerializedName("booking_period_key")
    @Expose
    private Integer bookingPeriodKey;
    @SerializedName("amount")
    @Expose
    private Integer amount;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getCataloguename() {
        return cataloguename;
    }

    public void setCataloguename(String cataloguename) {
        this.cataloguename = cataloguename;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getBookingPeriod() {
        return bookingPeriod;
    }

    public void setBookingPeriod(String bookingPeriod) {
        this.bookingPeriod = bookingPeriod;
    }

    public Integer getBookingPeriodKey() {
        return bookingPeriodKey;
    }

    public void setBookingPeriodKey(Integer bookingPeriodKey) {
        this.bookingPeriodKey = bookingPeriodKey;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
