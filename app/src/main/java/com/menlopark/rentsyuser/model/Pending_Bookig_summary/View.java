
package com.menlopark.rentsyuser.model.Pending_Bookig_summary;

import com.google.gson.annotations.SerializedName;

public class View {

    @SerializedName("amount")
    private Long mAmount;
    @SerializedName("booking_period")
    private String mBookingPeriod;
//    @SerializedName("photo1")
//    private String mCatalogueImg;
    @SerializedName("date")
    private String mDate;
    @SerializedName("item_name")
    private String mItemName;
    @SerializedName("item_id")
    private int item_id;
    @SerializedName("length")
    private Long mLength;
    @SerializedName("qty")
    private Long mQty;
    @SerializedName("photo1")
    private String mStoreImg;
    @SerializedName("store_name")
    private String mStoreName;
    @SerializedName("order_status ")
    private int order_status;
    @SerializedName("time")
    private String mTime;

    public Long getAmount() {
        return mAmount;
    }

    public int getOrder_status() {
        return order_status;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
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

//    public String getCatalogueImg() {
//        return mCatalogueImg;
//    }
//
//    public void setCatalogueImg(String catalogueImg) {
//        mCatalogueImg = catalogueImg;
//    }

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

    public String getStoreImg() {
        return mStoreImg;
    }

    public void setStoreImg(String storeImg) {
        mStoreImg = storeImg;
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
