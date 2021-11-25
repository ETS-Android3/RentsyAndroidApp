
package com.menlopark.rentsyuser.model.Notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("extend_booking_status")
    @Expose
    private int mExtendBookingStatus;
    @SerializedName("for_what")
    @Expose
    private String mForWhat;
    @SerializedName("message")
    @Expose
    private String mMessage;
    @SerializedName("notification_id")
    @Expose
    private int mNotificationId;
    @SerializedName("order_id")
    @Expose
    private int mOrderId;
    @SerializedName("rebook_status")
    @Expose
    private int mRebookStatus;
    @SerializedName("review_status")
    @Expose
    private int mReviewStatus;
    @SerializedName("title")
    @Expose
    private String mTitle;


    public int getmExtendBookingStatus() {
        return mExtendBookingStatus;
    }

    public void setmExtendBookingStatus(int mExtendBookingStatus) {
        this.mExtendBookingStatus = mExtendBookingStatus;
    }

    public String getmForWhat() {
        return mForWhat;
    }

    public void setmForWhat(String mForWhat) {
        this.mForWhat = mForWhat;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public int getmNotificationId() {
        return mNotificationId;
    }

    public void setmNotificationId(int mNotificationId) {
        this.mNotificationId = mNotificationId;
    }

    public int getmOrderId() {
        return mOrderId;
    }

    public void setmOrderId(int mOrderId) {
        this.mOrderId = mOrderId;
    }

    public int getmRebookStatus() {
        return mRebookStatus;
    }

    public void setmRebookStatus(int mRebookStatus) {
        this.mRebookStatus = mRebookStatus;
    }

    public int getmReviewStatus() {
        return mReviewStatus;
    }

    public void setmReviewStatus(int mReviewStatus) {
        this.mReviewStatus = mReviewStatus;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }


}
