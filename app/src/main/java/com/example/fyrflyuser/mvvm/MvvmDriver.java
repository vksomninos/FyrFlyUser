package com.example.fyrflyuser.mvvm;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fyrflyuser.modelClasses.CarTypeRoot;
import com.example.fyrflyuser.modelClasses.LoginRoot;
import com.example.fyrflyuser.modelClasses.OtpRoot;
import com.example.fyrflyuser.modelClasses.onlineOfflineRoot;
import com.example.fyrflyuser.retrofit.APIClient;
import com.example.fyrflyuser.retrofit.ApiInterface;
import com.example.fyrflyuser.retrofit.ApiInterfaceDriver;
import com.example.fyrflyuser.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MvvmDriver  extends ViewModel {
    ApiInterfaceDriver apiInterface = APIClient.getRetrofitClient().create(ApiInterfaceDriver.class);

    //   TODO :  OnlineOffline api


    private MutableLiveData<onlineOfflineRoot> onLineOfflineMLd;
    public LiveData<onlineOfflineRoot> OnlineOffline(Activity activity , String token , String DriverId , int status) {

        onLineOfflineMLd = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showDialog(activity, "Loading .......");
            apiInterface.onlineOffline(token, DriverId, status).enqueue(new Callback<onlineOfflineRoot>() {
                @Override
                public void onResponse(Call<onlineOfflineRoot> call, Response<onlineOfflineRoot> response) {

                    if (response.body() != null) {
                        CommonUtils.dismissDialog();
                        onLineOfflineMLd.postValue(response.body());

                    } else {
                        Toast.makeText(activity, "Technical  Issue", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<onlineOfflineRoot> call, Throwable t) {
                    CommonUtils.dismissDialog();

                    Toast.makeText(activity, "onFailure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Check  Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return onLineOfflineMLd;
    }

}
