
package com.menlopark.rentsyuser.model.Setting;


import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("detail")
    private Details mDetails;

    public Details getDetails() {
        return mDetails;
    }

    public void setDetails(Details details) {
        mDetails = details;
    }

}
