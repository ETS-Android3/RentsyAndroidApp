
package com.menlopark.rentsyuser.model.Catalogueitem;

import com.google.gson.annotations.SerializedName;

public class Data {

//    @SerializedName("details")
//    private List<Detail> mDetails;
//
//    public List<Detail> getDetails() {
//        return mDetails;
//    }
//
//    public void setDetails(List<Detail> details) {
//        mDetails = details;
//    }


    @SerializedName("details")
    private Detail mDetails;

    public Detail getDetails() {
        return mDetails;
    }

    public void setmDetails(Detail mDetails) {
        this.mDetails = mDetails;
    }
}
