
package com.menlopark.rentsyuser.model.HalpCata;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HelpCatRes {


    /*ArrayList<Data> list;


    public class Data{
        @SerializedName("id")
        public int id;

        @SerializedName("name")
        public int name;

        @SerializedName("type")
        public String type;

        @SerializedName("created")
        public String created;

        @SerializedName("modified")
        public String modified;
    }*/

//@SerializedName("data")
//  private Data data;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
   private String status;
  @SerializedName("data")
  private List<Detail> details;

//    public Data getData() {return data;}
//
//  public void setData(Data data) { this.data = data; }


  public List<Detail> getDetails() {
    return details;
  }

  public void setDetails(List<Detail> details) {
    this.details = details;
  }

  public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

}
