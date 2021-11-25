package com.menlopark.rentsyuser.model.Booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingParams {

    @SerializedName("booking_list")
    @Expose
    private List<BookingList> bookingList = null;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("final_total")
    @Expose
    private String finalTotal;

    public List<BookingList> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<BookingList> bookingList) {
        this.bookingList = bookingList;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(String finalTotal) {
        this.finalTotal = finalTotal;
    }
}
