
package com.menlopark.rentsyuser.model.Calendar_Data;

import java.util.List;
import com.google.gson.annotations.SerializedName;

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
