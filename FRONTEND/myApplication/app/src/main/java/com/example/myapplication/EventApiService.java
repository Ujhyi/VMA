package com.example.myapplication;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface EventApiService {

    @Multipart


    @POST("event/register")
    Call<ResponseBody> createEvent(
            @Part("user_id") RequestBody userId,
            @Part("title") RequestBody title,
            @Part("location") RequestBody location,
            @Part("date") RequestBody date,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part image
    );
}
