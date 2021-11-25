
package com.menlopark.rentsyuser.model.Pendding_Booking;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("view")
    private List<View> mView;

    public List<View> getView() {
        return mView;
    }

    public void setView(List<View> view) {
        mView = view;
    }

}
