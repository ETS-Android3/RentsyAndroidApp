package com.menlopark.rentsyuser.model.stores;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllSubCategory {

   // @SerializedName("subcategory_id")
   @SerializedName("id")
    @Expose
    public Integer subcategory_id;

    public Integer getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(Integer subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    @SerializedName("subcategory_name")
    @Expose
    public String subcategory_name;

}