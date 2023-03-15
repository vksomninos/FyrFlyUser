package com.example.fyrflyuser.utils;

import android.app.Application;
import android.content.Context;


public class App extends Application {

    private static App instance;
    private static PrefManager prefManager;
    Context context;



    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        context = getApplicationContext();
        prefManager = new PrefManager(context);


    }

    public static PrefManager getPrefManager(){
        return prefManager;
    }

    public static App getAppContext(){
        return instance;
    }


}
