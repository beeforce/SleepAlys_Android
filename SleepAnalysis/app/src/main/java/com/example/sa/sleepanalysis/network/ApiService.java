package com.example.sa.sleepanalysis.network;

import com.example.sa.sleepanalysis.model.NodeData;
import com.example.sa.sleepanalysis.model.SleepTime;
import com.example.sa.sleepanalysis.model.Userdetail;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @POST("login.php")
    @Multipart
    Call<AccessToken> login(@Part("email") RequestBody email, @Part("password") RequestBody password);

    @POST("register.php")
    @Multipart
    Call<AccessToken> register(@Part("email") RequestBody email, @Part("password") RequestBody password, @Part("name") RequestBody name,
                               @Part("dateofBirth") RequestBody dateofBirth);

    @GET("getUserdetails.php")
    Call<Userdetail> getUserdetail(@Query("id") int user_id);

    @POST("editUserdetail.php")
    @Multipart
    Call<AccessToken> updateUserdetail(@Part("id") RequestBody id, @Part("name") RequestBody name, @Part("password") RequestBody password,
                                       @Part("dateofBirth") RequestBody dateofBirth);

    @POST("forgetPassword.php")
    @Multipart
    Call<AccessToken> resetUserpassword(@Part("email") RequestBody email);

    @GET("getNodeData.php")
    Call<List<NodeData>> getNodeData(@Query("userID") int user_id);

    @GET("getHistory.php")
    Call<List<NodeData>> getHistorybyDate(@Query("userID") int user_id, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @GET("getHistory.php")
    Call<List<SleepTime>> getQualityTimebyDate(@Query("userID") String user_id, @Query("fromDate") String fromDate, @Query("toDate") String toDate);


    @GET("getHistory.php")
    Call<List<NodeData>> getHistorybyHours(@Query("userID") int user_id, @Query("hours") int hours, @Query("minutes") int minutes);

    @POST("addSleepingHours.php")
    @Multipart
    Call<AccessToken> addSleepingHours(@Part("userID") RequestBody user_id, @Part("hours") RequestBody hours);



}
