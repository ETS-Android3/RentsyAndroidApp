
package com.menlopark.rentsyuser.model.Rating;


import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("catalogue_id")
    private Long mCatalogueId;
    @SerializedName("photo1")
    private String mImages;
    @SerializedName("itemname")
    private String mItemname;

    public Long getCatalogueId() {
        return mCatalogueId;
    }

    public void setCatalogueId(Long catalogueId) {
        mCatalogueId = catalogueId;
    }

    public String getImages() {
        return mImages;
    }

    public void setImages(String images) {
        mImages = images;
    }

    public String getItemname() {
        return mItemname;
    }

    public void setItemname(String itemname) {
        mItemname = itemname;
    }

}
