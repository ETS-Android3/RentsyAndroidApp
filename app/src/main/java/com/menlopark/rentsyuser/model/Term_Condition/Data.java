
package com.menlopark.rentsyuser.model.Term_Condition;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("details")
    private List<Details>  mDetails;

    public List<Details> getmDetails() {
        return mDetails;
    }

    public void setmDetails(List<Details> mDetails) {
        this.mDetails = mDetails;
    }

//    public Details getDetails() {
//        return mDetails;
//    }
//
//    public void setDetails(Details details) {
//        mDetails = details;
//    }

}
