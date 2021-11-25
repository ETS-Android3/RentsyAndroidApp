
package com.menlopark.rentsyuser.model.Facebook_Model;

import com.google.gson.annotations.SerializedName;

public class Customer {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("app_token")
    private String mAppToken;
    @SerializedName("contact_no")
    private String mContactNo;
    @SerializedName("created")
    private String mCreated;
    @SerializedName("device_token")
    private String mDeviceToken;
    @SerializedName("device_type")
    private Object mDeviceType;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("fb_id")
    private String mFbId;
    @SerializedName("id")
    private Long mId;
    @SerializedName("img_name")
    private String mImgName;
    @SerializedName("last_login")
    private Object mLastLogin;
    @SerializedName("location_id")
    private Long mLocationId;
    @SerializedName("modified")
    private String mModified;
    @SerializedName("name")
    private String mName;
    @SerializedName("status")
    private Long mStatus;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getAppToken() {
        return mAppToken;
    }

    public void setAppToken(String appToken) {
        mAppToken = appToken;
    }

    public String getContactNo() {
        return mContactNo;
    }

    public void setContactNo(String contactNo) {
        mContactNo = contactNo;
    }

    public String getCreated() {
        return mCreated;
    }

    public void setCreated(String created) {
        mCreated = created;
    }

    public String getDeviceToken() {
        return mDeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        mDeviceToken = deviceToken;
    }

    public Object getDeviceType() {
        return mDeviceType;
    }

    public void setDeviceType(Object deviceType) {
        mDeviceType = deviceType;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getFbId() {
        return mFbId;
    }

    public void setFbId(String fbId) {
        mFbId = fbId;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getImgName() {
        return mImgName;
    }

    public void setImgName(String imgName) {
        mImgName = imgName;
    }

    public Object getLastLogin() {
        return mLastLogin;
    }

    public void setLastLogin(Object lastLogin) {
        mLastLogin = lastLogin;
    }

    public Long getLocationId() {
        return mLocationId;
    }

    public void setLocationId(Long locationId) {
        mLocationId = locationId;
    }

    public String getModified() {
        return mModified;
    }

    public void setModified(String modified) {
        mModified = modified;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
