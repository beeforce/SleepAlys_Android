package com.example.sa.sleepanalysis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Userdetail implements Serializable {



    @SerializedName("success")
    @Expose
    boolean success;

    @SerializedName("data")
    @Expose
    User data;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }




    public class User {
        @SerializedName("id")
        @Expose
        int id;

        @SerializedName("name")
        @Expose
        String name;

        @SerializedName("age")
        @Expose
        int age;

        @SerializedName("dateofBirth")
        @Expose
        String dateofBirth;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @SerializedName("email")
        @Expose
        String email;

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

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getDateofBirth() {
            return dateofBirth;
        }

        public void setDateofBirth(String dateofBirth) {
            this.dateofBirth = dateofBirth;
        }

    }

}
