package com.example.fyrflyuser.mvvm;

import android.app.Activity;
import android.media.session.MediaSession;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fyrflyuser.modelClasses.CarTypeRoot;
import com.example.fyrflyuser.modelClasses.ConvertDriverRoot;
import com.example.fyrflyuser.modelClasses.DriverNearBy;
import com.example.fyrflyuser.modelClasses.LoginRoot;
import com.example.fyrflyuser.modelClasses.OtpRoot;
import com.example.fyrflyuser.modelClasses.ProfileRoot;
import com.example.fyrflyuser.modelClasses.UserDetailsRoot;
import com.example.fyrflyuser.retrofit.APIClient;
import com.example.fyrflyuser.retrofit.ApiInterface;
import com.example.fyrflyuser.utils.CommonUtils;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;

public class Mvvm extends ViewModel {
    ApiInterface apiInterface = APIClient.getRetrofitClient().create(ApiInterface.class);

    //TODO :  unique
    private MutableLiveData<LoginRoot> unique;

    public LiveData<LoginRoot> LoginPhone(Activity activity, String phone) {

        unique = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showDialog(activity, "Loading .......");
            apiInterface.loginPhone(phone).enqueue(new Callback<LoginRoot>() {
                @Override
                public void onResponse(Call<LoginRoot> call, Response<LoginRoot> response) {

                    if (response.body() != null) {
                        CommonUtils.dismissDialog();
                        unique.postValue(response.body());
                    } else {
                        Toast.makeText(activity, "Technical  Issue", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginRoot> call, Throwable t) {
                    CommonUtils.dismissDialog();

                    Toast.makeText(activity, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Check  Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return unique;


    }

    //   TODO :  Otp verify api
    private MutableLiveData<OtpRoot> otpVerifyMLD;

    public LiveData<OtpRoot> otpVerify(Activity activity, String phone, String otp) {

        otpVerifyMLD = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showDialog(activity, "Loading .......");
            apiInterface.verifyOtp(phone, otp).enqueue(new Callback<OtpRoot>() {
                @Override
                public void onResponse(Call<OtpRoot> call, Response<OtpRoot> response) {

                    if (response.body() != null) {
                        CommonUtils.dismissDialog();
                        otpVerifyMLD.postValue(response.body());

                    } else {
                        Toast.makeText(activity, "Technical  Issue", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OtpRoot> call, Throwable t) {
                    CommonUtils.dismissDialog();

                    Toast.makeText(activity, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Check  Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return otpVerifyMLD;


    }

    //   TODO :  editProfile api

    private MutableLiveData<ProfileRoot> editProfileMLD;

    public LiveData<ProfileRoot> editProfile(Activity activity, String Token, RequestBody userId, RequestBody firstname, RequestBody lastname, RequestBody email, RequestBody phone, MultipartBody.Part profileImage) {

        editProfileMLD = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showDialog(activity, "Loading .......");
            apiInterface.editProfile(Token, userId,firstname, lastname, email, phone, profileImage).enqueue(new Callback<ProfileRoot>() {
                @Override
                public void onResponse(Call<ProfileRoot> call, Response<ProfileRoot> response) {

                    if (response.body() != null) {
                        CommonUtils.dismissDialog();
                        editProfileMLD.postValue(response.body());

                    } else {
                        Toast.makeText(activity, "Technical  Issue", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileRoot> call, Throwable t) {
                    CommonUtils.dismissDialog();
                    Log.d("onFailure",t.getMessage());
                    Toast.makeText(activity, "onFailure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Check  Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return editProfileMLD;
    }



    //   TODO :  userDetail api

    private MutableLiveData<UserDetailsRoot> userDetailMLD;
    public LiveData<UserDetailsRoot> userDetails(Activity activity,String Token, String userId) {

        userDetailMLD = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.userDetails(Token,userId).enqueue(new Callback<UserDetailsRoot>() {
                @Override
                public void onResponse(Call<UserDetailsRoot> call, Response<UserDetailsRoot> response) {

                    if (response.body() != null) {
                        userDetailMLD.postValue(response.body());

                    } else {
                        Toast.makeText(activity, "Technical  Issue", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserDetailsRoot> call, Throwable t) {

                    Toast.makeText(activity, "onFailure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Check  Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return userDetailMLD;
    }


    //   TODO :  applyDriver api

    private MutableLiveData<ConvertDriverRoot> converToDriverMLD;
    public LiveData<ConvertDriverRoot> applyForDriver(Activity activity, String token, RequestBody userId,RequestBody cartype,
                                                RequestBody brand_name,RequestBody city,RequestBody vehicle_model,RequestBody vehicle_registration_number,RequestBody vehicle_or_driver_info,MultipartBody.Part driver_license) {

        converToDriverMLD = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showDialog(activity, "Loading .......");
            apiInterface.applyfordriver(token,userId, cartype, brand_name, city ,vehicle_model, vehicle_registration_number, vehicle_or_driver_info,driver_license).enqueue(new Callback<ConvertDriverRoot>() {
                @Override
                public void onResponse(Call<ConvertDriverRoot> call, Response<ConvertDriverRoot> response) {

                    if (response.body() != null) {
                        CommonUtils.dismissDialog();
                        converToDriverMLD.postValue(response.body());

                    } else {
                        Toast.makeText(activity, "Technical  Issue", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ConvertDriverRoot> call, Throwable t) {
                    CommonUtils.dismissDialog();

                    Toast.makeText(activity, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Check  Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return converToDriverMLD;
    }


    //   TODO :  CarTypes api

    private MutableLiveData<CarTypeRoot> carTyperMLD;
    public LiveData<CarTypeRoot> carType(Activity activity , String token) {

        carTyperMLD = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showDialog(activity, "Loading .......");
            apiInterface.carCategories(token).enqueue(new Callback<CarTypeRoot>() {
                @Override
                public void onResponse(Call<CarTypeRoot> call, Response<CarTypeRoot> response) {

                    if (response.body() != null) {
                        CommonUtils.dismissDialog();
                        carTyperMLD.postValue(response.body());

                    } else {
                        Toast.makeText(activity, "Technical  Issue", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CarTypeRoot> call, Throwable t) {
                    CommonUtils.dismissDialog();

                    Toast.makeText(activity, "onFailure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Check  Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return carTyperMLD;
    }

    //   TODO :  logOut api

    private MutableLiveData<LoginRoot> logOutMLd;
    public LiveData<LoginRoot> logOut(Activity activity , String tokenheader ,String token) {

        logOutMLd = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showDialog(activity, "Loading .......");
            apiInterface.logOut(tokenheader,token).enqueue(new Callback<LoginRoot>() {
                @Override
                public void onResponse(Call<LoginRoot> call, Response<LoginRoot> response) {

                    if (response.body() != null) {
                        CommonUtils.dismissDialog();
                        logOutMLd.postValue(response.body());

                    } else {
                        Toast.makeText(activity, "Technical  Issue", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginRoot> call, Throwable t) {
                    CommonUtils.dismissDialog();

                    Toast.makeText(activity, "onFailure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Check  Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return logOutMLd;
    }


    //   TODO :  DriveNearBy  api

    private MutableLiveData<DriverNearBy> DriverNearByMLD;
    public LiveData<DriverNearBy> DriverNearBy(Activity activity , String tokenheader ,String city ,String lat ,String lng) {

        DriverNearByMLD = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.nearByDrivers(tokenheader,city,lat,lng).enqueue(new Callback<DriverNearBy>() {
                @Override
                public void onResponse(Call<DriverNearBy> call, Response<DriverNearBy> response) {

                    if (response.body() != null) {
                        DriverNearByMLD.postValue(response.body());

                    } else {
                        Toast.makeText(activity, "Technical  Issue", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DriverNearBy> call, Throwable t) {

                    Toast.makeText(activity, "onFailure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Check  Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return DriverNearByMLD;
    }

}
