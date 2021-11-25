package com.menlopark.rentsyuser.model.Booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingList {

    @SerializedName("booking_fees")
    @Expose
    private Integer bookingFees;
    @SerializedName("cart_id")
    @Expose
    private Integer cartId;
    @SerializedName("discount_no")
    @Expose
    private Integer discountNo;
    @SerializedName("promo_code")
    @Expose
    private String promoCode;

    public Integer getBookingFees() {
        return bookingFees;
    }

    public void setBookingFees(Integer bookingFees) {
        this.bookingFees = bookingFees;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getDiscountNo() {
        return discountNo;
    }

    public void setDiscountNo(Integer discountNo) {
        this.discountNo = discountNo;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

}
