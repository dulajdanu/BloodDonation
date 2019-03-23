package com.example.blooddonation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("push_notication")
    Call<ResponseBody>sendPushMsg(
            @Field("devicetoken") String devToken
    );


}
