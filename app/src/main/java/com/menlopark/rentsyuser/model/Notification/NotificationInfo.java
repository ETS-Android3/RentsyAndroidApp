
package com.menlopark.rentsyuser.model.Notification;


import com.google.gson.annotations.SerializedName;

public class NotificationInfo{

    @SerializedName("extend_booking_status")
    private Long mExtendBookingStatus;
    @SerializedName("for_what")
    private String mForWhat;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("notification_id")
    private Long mNotificationId;
    @SerializedName("order_id")
    private Long mOrderId;
    @SerializedName("rebook_status")
    private Long mRebookStatus;
    @SerializedName("review_status")
    private Long mReviewStatus;
    @SerializedName("title")
    private String mTitle;

    public Long getExtendBookingStatus() {
        return mExtendBookingStatus;
    }

    public void setExtendBookingStatus(Long extendBookingStatus) {
        mExtendBookingStatus = extendBookingStatus;
    }

    public String getForWhat() {
        return mForWhat;
    }

    public void setForWhat(String forWhat) {
        mForWhat = forWhat;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getNotificationId() {
        return mNotificationId;
    }

    public void setNotificationId(Long notificationId) {
        mNotificationId = notificationId;
    }

    public Long getOrderId() {
        return mOrderId;
    }

    public void setOrderId(Long orderId) {
        mOrderId = orderId;
    }

    public Long getRebookStatus() {
        return mRebookStatus;
    }

    public void setRebookStatus(Long rebookStatus) {
        mRebookStatus = rebookStatus;
    }

    public Long getReviewStatus() {
        return mReviewStatus;
    }

    public void setReviewStatus(Long reviewStatus) {
        mReviewStatus = reviewStatus;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
