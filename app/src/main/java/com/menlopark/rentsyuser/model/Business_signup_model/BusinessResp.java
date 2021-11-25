
package com.menlopark.rentsyuser.model.Business_signup_model;

import com.google.gson.annotations.SerializedName;

public class BusinessResp {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private String mStatus;

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }


}
