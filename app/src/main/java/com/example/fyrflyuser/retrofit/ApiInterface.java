package com.example.fyrflyuser.retrofit;

import com.example.fyrflyuser.modelClasses.CarTypeRoot;
import com.example.fyrflyuser.modelClasses.ConvertDriverRoot;
import com.example.fyrflyuser.modelClasses.DriverNearBy;
import com.example.fyrflyuser.modelClasses.LoginRoot;
import com.example.fyrflyuser.modelClasses.OtpRoot;
import com.example.fyrflyuser.modelClasses.ProfileRoot;
import com.example.fyrflyuser.modelClasses.UserDetailsRoot;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("generate-otp")
    Call<LoginRoot> loginPhone(@Field("phone") String phone);


    @FormUrlEncoded
    @POST("verify-otp")
    Call<OtpRoot> verifyOtp(
            @Field("phone") String phone,
            @Field("otp") String otp);


    @Multipart
    @POST("edit-profile")
    Call<ProfileRoot> editProfile(
            @Header("LOGIN_TOKEN") String LOGIN_TOKEN,
            @Part("userId") RequestBody userId,
            @Part("firstname") RequestBody firstname,
            @Part("lastname") RequestBody lastname,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part MultipartBody.Part profileImage);


    @FormUrlEncoded
    @POST("get-user-details")
    Call<UserDetailsRoot> userDetails(
            @Header("LOGIN_TOKEN") String LOGIN_TOKEN,
            @Field("userId") String phone);


    @Multipart
    @POST("apply-for-driver")
    Call<ConvertDriverRoot> applyfordriver(
            @Header("LOGIN_TOKEN") String LOGIN_TOKEN,
            @Part("userId") RequestBody userId,
            @Part("cartype") RequestBody cartype,
            @Part("city") RequestBody city,
            @Part("brand_name") RequestBody brand_name,
            @Part("vehicle_model") RequestBody vehicle_model,
            @Part("vehicle_registration_number") RequestBody vehicle_registration_number,
            @Part("vehicle_or_driver_info") RequestBody vehicle_or_driver_info,
            @Part MultipartBody.Part driver_license);


    @GET("car-categories")
    Call<CarTypeRoot> carCategories(
            @Header("LOGIN_TOKEN") String LOGIN_TOKEN

    );


    @FormUrlEncoded
    @POST("log-out")
    Call<LoginRoot> logOut(
            @Header("LOGIN_TOKEN") String LOGIN_TOKEN,
             @Field("auth_token") String auth_token);


    @FormUrlEncoded
    @POST("get-near-by-drivers")
    Call<DriverNearBy> nearByDrivers(
            @Header("LOGIN_TOKEN") String LOGIN_TOKEN,
             @Field("city") String city,
             @Field("lat") String lat,
             @Field("long") String lng);




}
