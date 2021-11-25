package com.menlopark.rentsyuser.model.Add_Booking;

public class AddBookingParams {

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public String getBooking_fees() {
        return booking_fees;
    }

    public void setBooking_fees(String booking_fees) {
        this.booking_fees = booking_fees;
    }

    private String cart_id;
    private  String discount;
    private  String promocode;
    private String booking_fees;
    private  String transectionid;

    public String getTransectionid() {
        return transectionid;
    }

    public void setTransectionid(String transectionid) {
        this.transectionid = transectionid;
    }


}
