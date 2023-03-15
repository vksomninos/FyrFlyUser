package com.example.fyrflyuser.retrofit;

import com.example.fyrflyuser.modelClasses.onlineOfflineRoot;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterfaceDriver {

    @FormUrlEncoded
    @POST("change-driver-status")
    Call<onlineOfflineRoot> onlineOffline(
            @Header("LOGIN_TOKEN") String LOGIN_TOKEN,
            @Field("driverId") String driverId,
            @Field("Status") int Status);


}
