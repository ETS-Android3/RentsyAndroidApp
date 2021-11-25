
package com.menlopark.rentsyuser.model.Calendar_Data;


import com.google.gson.annotations.SerializedName;

public class View {

    @SerializedName("booking_period")
    private String mBookingPeriod;
    @SerializedName("order_id")
    private int id;
    @SerializedName("photo1")
    private String mCatalogueImg;
    @SerializedName("date_time")
    private String mDateTime;
    @SerializedName("item_name")
    private String mItemName;
    @SerializedName("length")
    private Long mLength;
    @SerializedName("store_name")
    private String mStoreName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String dateTime) {
        mDateTime = dateTime;
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

    public String getStoreName() {
        return mStoreName;
    }

    public void setStoreName(String storeName) {
        mStoreName = storeName;
    }

}
