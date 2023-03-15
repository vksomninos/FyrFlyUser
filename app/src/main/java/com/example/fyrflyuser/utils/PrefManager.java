package com.example.fyrflyuser.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

public class PrefManager {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public PrefManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("FyrFly", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public String getPreferences(String key){
        return sharedPreferences.getString(key,"");
    }

    public void clearPreferences(){
        editor.clear();
        editor.apply();
    }

    public <T> T getModel(String key, Class<T> type) {

        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(key, ""), type);
    }

    public void saveModel(String key, Object modelClass){

        Gson gson = new Gson();
        editor.putString(key, gson.toJson(modelClass));
        editor.apply();
        Log.d("docWorld", "Model saved");
    }



    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public void saveLong(String key, Long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }


    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }
    public void saveUserLogin(String login){
        editor.putString(AppConstants.LOGIN_KEY,login);
        editor.commit();
        Log.d("docWorld", "Save User Login");
    }

    public void saveUserMobile(String mobile){
        editor.putString(AppConstants.MOBILE_NUMBER,mobile);
        editor.commit();
        Log.d("docWorld", "Save mobile Login");
    }

    public void userLogOut(){
        editor.remove(AppConstants.LOGIN_KEY);
        editor.commit();
        Log.d("docWorld", "Save User Logout");
    }

    public boolean isUserLogin(){
        boolean isUser = sharedPreferences.getString(AppConstants.LOGIN_KEY,"").isEmpty();
        Log.d("docWorld", ""+isUser);
        return !isUser;
    }


        public void saveStringValue(String key, String value) {
            editor.putString(key, value);
            editor.apply();
        }

        public String getStringValue(String key) {
            return sharedPreferences.getString(key, "");
        }






        public void saveUserId(String userid) {
            editor.putString(AppConstants.USER_ID, userid);
            editor.commit();
            Log.d("docWorld", "Save User Id");
        }


        public void saveUserImage(String image) {
            editor.putString(AppConstants.USER_IMAGE, image);
            editor.commit();
            Log.d("docWorld", "Save user image");
        }




    }
