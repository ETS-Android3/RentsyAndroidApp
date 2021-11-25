package com.menlopark.rentsyuser.model.ordercancel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class requestCancel {

    @Expose
    @SerializedName("catalogue_id")
    private int catalogueId;

    @Expose
    @SerializedName("order_id")
    private int orderId;

    public int getCatalogueId() {
        return catalogueId;
    }

    public int getOrderId() {
        return orderId;
    }

	public void setCatalogueId(int catalogueId) {
		this.catalogueId = catalogueId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
}