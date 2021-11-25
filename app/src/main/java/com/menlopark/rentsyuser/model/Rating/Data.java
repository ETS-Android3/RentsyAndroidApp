
package com.menlopark.rentsyuser.model.Rating;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("details")
    private List<Detail> mDetails;

    public List<Detail> getDetails() {
        return mDetails;
    }

    public void setDetails(List<Detail> details) {
        mDetails = details;
    }

}
