
package com.menlopark.rentsyuser.model.Notification;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("notification_info")
    private List<Detail> mNotificationInfo;
    @SerializedName("total_record")
    private Long mTotalRecord;

    public List<Detail> getNotificationInfo() {
        return mNotificationInfo;
    }

    public void setNotificationInfo(List<Detail> notificationInfo) {
        mNotificationInfo = notificationInfo;
    }

    public Long getTotalRecord() {
        return mTotalRecord;
    }

    public void setTotalRecord(Long totalRecord) {
        mTotalRecord = totalRecord;
    }

}
