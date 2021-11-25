
package com.menlopark.rentsyuser.model.stores.Catalogs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("photo1")
    @Expose
    private String images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("itemname")
    @Expose
    private String itemname;
    @SerializedName("categoryname")
    @Expose
    private String categoryname;
    @SerializedName("bookingrate")
    @Expose
    private Integer bookingrate;
    @SerializedName("minbookingperiod")
    @Expose
    private String minbookingperiod;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public Integer getBookingrate() {
        return bookingrate;
    }

    public void setBookingrate(Integer bookingrate) {
        this.bookingrate = bookingrate;
    }

    public String getMinbookingperiod() {
        return minbookingperiod;
    }

    public void setMinbookingperiod(String minbookingperiod) {
        this.minbookingperiod = minbookingperiod;
    }

}
