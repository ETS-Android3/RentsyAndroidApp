
package com.menlopark.rentsyuser.model.HalpCata;


import com.google.gson.annotations.SerializedName;

public class Detail {

    public static final int SINGLE_TYPE = 0;
    public static final int DOUBLE_TYPE = 1;

    public int typeid;

    public Detail(String type, int id, String typeName) {
        this.type = type;
        this.id = id;
        //this.subCategory = subCategory;
        this.type = typeName;
    }



//    @SerializedName("sub_category")
//    private List<SubCategory> subCategory;

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public List<SubCategory> getSubCategory() {
//        return subCategory;
//    }
//
//    public void setSubCategory(List<SubCategory> subCategory) {
//        this.subCategory = subCategory;
//    }

    public String getTypeName() {
        return type;
    }

    public void setTypeName(String typeName) {
        this.type = typeName;
    }

}
