
package com.menlopark.rentsyuser.model.Cart_Detail;

import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("amount")
    private Long mAmount;
    @SerializedName("Booking_Fee")
    private Long mBookingFee;
    @SerializedName("bookingrate")
    private String mBookingrate;
    @SerializedName("business_address")
    private String mBusinessAddress;
    @SerializedName("business_name")
    private String mBusinessName;
    @SerializedName("cart_id")
    private Long mCartId;
    @SerializedName("catalogue_name")
    private String mCatalogueName;
    @SerializedName("date")
    private String mDate;
    @SerializedName("Discount")
    private Long mDiscount;
    @SerializedName("images")
    private String mImages;
    @SerializedName("length")
    private String mLength;
    @SerializedName("location_name")
    private String mLocationName;
    @SerializedName("qty")
    private Long mQty;
    @SerializedName("time")
    private String mTime;

    public Long getAmount() {
        return mAmount;
    }

    public void setAmount(Long amount) {
        mAmount = amount;
    }

    public Long getBookingFee() {
        return mBookingFee;
    }

    public void setBookingFee(Long bookingFee) {
        mBookingFee = bookingFee;
    }

    public String getBookingrate() {
        return mBookingrate;
    }

    public void setBookingrate(String bookingrate) {
        mBookingrate = bookingrate;
    }

    public String getBusinessAddress() {
        return mBusinessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        mBusinessAddress = businessAddress;
    }

    public String getBusinessName() {
        return mBusinessName;
    }

    public void setBusinessName(String businessName) {
        mBusinessName = businessName;
    }

    public Long getCartId() {
        return mCartId;
    }

    public void setCartId(Long cartId) {
        mCartId = cartId;
    }

    public String getCatalogueName() {
        return mCatalogueName;
    }

    public void setCatalogueName(String catalogueName) {
        mCatalogueName = catalogueName;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public Long getDiscount() {
        return mDiscount;
    }

    public void setDiscount(Long discount) {
        mDiscount = discount;
    }

    public String getImages() {
        return mImages;
    }

    public void setImages(String images) {
        mImages = images;
    }

    public String getLength() {
        return mLength;
    }

    public void setLength(String length) {
        mLength = length;
    }

    public String getLocationName() {
        return mLocationName;
    }

    public void setLocationName(String locationName) {
        mLocationName = locationName;
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
