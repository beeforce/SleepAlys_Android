package com.example.sa.sleepanalysis.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessToken {

    @SerializedName("success")
    @Expose
    boolean success;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("user_id")
    @Expose
    int user_id;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setUser_id(int id){
        this.user_id = id;
    }

    public int getUser_id() {
        return user_id;
    }
}
