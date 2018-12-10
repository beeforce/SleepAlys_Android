package com.example.sa.sleepanalysis.network;

public class ApiUtils {
    private ApiUtils() {
    }

    //    public static final String BASE_URL = "http://192.168.1.13:80/UPint/public/api/";
    public static final String BASE_URL = "http://10.0.2.2:80/sa/api/";

    public static ApiService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
