
package com.menlopark.rentsyuser.model.Setting;

import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("current_location")
    private String mCurrentLocation;
    @SerializedName("email_alerts_status")
    private Long mEmailAlertsStatus;
    @SerializedName("email_invoice_status")
    private Long mEmailInvoiceStatus;
    @SerializedName("location_id")
    private Long mLocationId;
    @SerializedName("push_notification_status")
    private Long mPushNotificationStatus;
    @SerializedName("radious")
    private Long mRadious;

    public String getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        mCurrentLocation = currentLocation;
    }

    public Long getEmailAlertsStatus() {
        return mEmailAlertsStatus;
    }

    public void setEmailAlertsStatus(Long emailAlertsStatus) {
        mEmailAlertsStatus = emailAlertsStatus;
    }

    public Long getEmailInvoiceStatus() {
        return mEmailInvoiceStatus;
    }

    public void setEmailInvoiceStatus(Long emailInvoiceStatus) {
        mEmailInvoiceStatus = emailInvoiceStatus;
    }

    public Long getLocationId() {
        return mLocationId;
    }

    public void setLocationId(Long locationId) {
        mLocationId = locationId;
    }

    public Long getPushNotificationStatus() {
        return mPushNotificationStatus;
    }

    public void setPushNotificationStatus(Long pushNotificationStatus) {
        mPushNotificationStatus = pushNotificationStatus;
    }

    public Long getRadious() {
        return mRadious;
    }

    public void setRadious(Long radious) {
        mRadious = radious;
    }

}
