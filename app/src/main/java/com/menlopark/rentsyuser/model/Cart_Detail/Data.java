
package com.menlopark.rentsyuser.model.Cart_Detail;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("details")
    private Details mDetails;

    public Details getDetails() {
        return mDetails;
    }

    public void setDetails(Details details) {
        mDetails = details;
    }

}
