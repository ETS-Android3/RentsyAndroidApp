package com.menlopark.rentsyuser.model.HalpCata;

import com.google.gson.annotations.SerializedName;

public class HelpDetails {
    @SerializedName("id")
    public int  id;

    @SerializedName("name")
    public String name;

    @SerializedName("category_name")
    public String category_name;

    @SerializedName("type")
    public int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
