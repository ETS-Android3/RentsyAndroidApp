
package com.menlopark.rentsyuser.model.Booking_Summary;

import com.google.gson.annotations.SerializedName;

public class View {

    @SerializedName("amount")
    private Long mAmount;
    @SerializedName("booking_period")
    private String mBookingPeriod;
    @SerializedName("bookingrate")
    private Long mBookingrate;
    @SerializedName("catalogue_img")
    private String mCatalogueImg;
    @SerializedName("date")
    private String mDate;
    @SerializedName("item_name")
    private String mItemName;
    @SerializedName("length")
    private Long mLength;
    @SerializedName("qty")
    private Long mQty;
    @SerializedName("store_name")
    private String mStoreName;
    @SerializedName("time")
    private String mTime;

    public Long getAmount() {
        return mAmount;
    }

    public void setAmount(Long amount) {
        mAmount = amount;
    }

    public String getBookingPeriod() {
        return mBookingPeriod;
    }

    public void setBookingPeriod(String bookingPeriod) {
        mBookingPeriod = bookingPeriod;
    }

    public Long getBookingrate() {
        return mBookingrate;
    }

    public void setBookingrate(Long bookingrate) {
        mBookingrate = bookingrate;
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

    public Long getQty() {
        return mQty;
    }

    public void setQty(Long qty) {
        mQty = qty;
    }

    public String getStoreName() {
        return mStoreName;
    }

    public void setStoreName(String storeName) {
        mStoreName = storeName;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

}
