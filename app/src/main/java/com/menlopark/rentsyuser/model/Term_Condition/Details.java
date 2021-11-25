
package com.menlopark.rentsyuser.model.Term_Condition;

import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("category")
    private int mCategory;
    @SerializedName("created")
    private String mCreated;
    @SerializedName("details")
    private String mDetails;
    @SerializedName("id")
    private int mId;
    @SerializedName("modified")
    private String mModified;

    public int getCategory() {
        return mCategory;
    }

    public void setCategory(int category) {
        mCategory = category;
    }

    public String getCreated() {
        return mCreated;
    }

    public void setCreated(String created) {
        mCreated = created;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getModified() {
        return mModified;
    }

    public void setModified(String modified) {
        mModified = modified;
    }

}
