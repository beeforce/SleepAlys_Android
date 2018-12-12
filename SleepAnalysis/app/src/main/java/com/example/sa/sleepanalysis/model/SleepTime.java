package com.example.sa.sleepanalysis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SleepTime implements Serializable{


    @SerializedName("created_at")
    @Expose
    String created_at;

    @SerializedName("user_sleeptime_id")
    @Expose
    String user_sleeptime_id;

    @SerializedName("quantity_hours")
    @Expose
    String quantity_hours;

    @SerializedName("user_Id")
    @Expose
    String user_Id;



    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUser_sleeptime_id() {
        return user_sleeptime_id;
    }

    public void setUser_sleeptime_id(String user_sleeptime_id) {
        this.user_sleeptime_id = user_sleeptime_id;
    }

    public String getQuantity_hours() {
        return quantity_hours;
    }

    public void setQuantity_hours(String quantity_hours) {
        this.quantity_hours = quantity_hours;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }


}
