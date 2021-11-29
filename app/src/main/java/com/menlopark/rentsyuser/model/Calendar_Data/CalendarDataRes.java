
package com.menlopark.rentsyuser.model.Calendar_Data;

import com.google.gson.annotations.SerializedName;

public class CalendarDataRes {

    @SerializedName("data")
    private Data mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private String mStatus;

    public CalendarDataRes(Data mData, String mMessage, String mStatus) {
        this.mData = mData;
        this.mMessage = mMessage;
        this.mStatus = mStatus;
    }

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
