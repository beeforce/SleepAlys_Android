package com.example.sa.sleepanalysis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NodeData implements Serializable{



    @SerializedName("humidity")
    @Expose
    String humidity;

    @SerializedName("temperature")
    @Expose
    String temperature;

    @SerializedName("vibration")
    @Expose
    String vibration;

    public String getNode_data_id() {
        return node_data_id;
    }

    public void setNode_data_id(String node_data_id) {
        this.node_data_id = node_data_id;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    @SerializedName("node_data_id")
    @Expose
    String node_data_id;


    @SerializedName("user_Id")
    @Expose
    String user_Id;

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getVibration() {
        return vibration;
    }

    public void setVibration(String vibration) {
        this.vibration = vibration;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @SerializedName("created_at")
    @Expose
    String created_at;
}
