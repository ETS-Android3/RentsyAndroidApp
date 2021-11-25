
package com.menlopark.rentsyuser.model.Catalogueitem;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Detail {

    @SerializedName("bookingrate")
    private Long mBookingrate;
    @SerializedName("categoryname")
    private String mCategoryname;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("images")
    private List<String> mImages;
    @SerializedName("itemname")
    private String mItemname;
    @SerializedName("latitude")
    private String mLatitude;
    @SerializedName("longitude")
    private String mLongitude;
    @SerializedName("minbookingperiod")
    private String mMinbookingperiod;
    @SerializedName("rating")
    private Integer mRating;
    @SerializedName("storeaddress")
    private String mStoreaddress;
    @SerializedName("subcategoryname")
    private String mSubcategoryname;

    @SerializedName("id")
    private Integer id;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("photo1")
    private String photo1;

    @SerializedName("photo2")
    private String photo2;

    @SerializedName("photo3")
    private String photo3;

    @SerializedName("photo4")
    private String photo4;

    @SerializedName("photo5")
    private String photo5;


    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getPhoto5() {
        return photo5;
    }

    public void setPhoto5(String photo5) {
        this.photo5 = photo5;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Long getBookingrate() {
        return mBookingrate;
    }

    public void setBookingrate(Long bookingrate) {
        mBookingrate = bookingrate;
    }

    public String getCategoryname() {
        return mCategoryname;
    }

    public void setCategoryname(String categoryname) {
        mCategoryname = categoryname;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public List<String> getImages() {
        return mImages;
    }

    public void setImages(List<String> images) {
        mImages = images;
    }

    public String getItemname() {
        return mItemname;
    }

    public void setItemname(String itemname) {
        mItemname = itemname;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getMinbookingperiod() {
        return mMinbookingperiod;
    }

    public void setMinbookingperiod(String minbookingperiod) {
        mMinbookingperiod = minbookingperiod;
    }

    public Integer getRating() {
        return mRating;
    }

    public void setRating(Integer rating) {
        mRating = rating;
    }

    public String getStoreaddress() {
        return mStoreaddress;
    }

    public void setStoreaddress(String storeaddress) {
        mStoreaddress = storeaddress;
    }

    public String getSubcategoryname() {
        return mSubcategoryname;
    }

    public void setSubcategoryname(String subcategoryname) {
        mSubcategoryname = subcategoryname;
    }

}
