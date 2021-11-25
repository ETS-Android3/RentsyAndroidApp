
package com.menlopark.rentsyuser.model.Pendding_Booking;


import com.google.gson.annotations.SerializedName;

public class View {

    @SerializedName("booking_period")
    private String mBookingPeriod;
    @SerializedName("photo1")
    private String mCatalogueImg;
    @SerializedName("date")
    private String mDate;
    @SerializedName("item_name")
    private String mItemName;
    @SerializedName("length")
    private Long mLength;
    @SerializedName("order_id")
    private Long mOrderId;
    @SerializedName("qty")
    private Long mQty;
    @SerializedName("time")
    private String mTime;

    public String getBookingPeriod() {
        return mBookingPeriod;
    }

    public void setBookingPeriod(String bookingPeriod) {
        mBookingPeriod = bookingPeriod;
    }

    public String getCatalogueImg() {
        return mCatalogueImg;
    }

    public void setCatalogueImg(String catalogueImg) {
        mCatalogueImg = catalogueImg;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        mItemName = itemName;
    }

    public Long getLength() {
        return mLength;
    }

    public void setLength(Long length) {
        mLength = length;
    }

    public Long getOrderId() {
        return mOrderId;
    }

    public void setOrderId(Long orderId) {
        mOrderId = orderId;
    }

    public Long getQty() {
        return mQty;
    }

    public void setQty(Long qty) {
        mQty = qty;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

}
