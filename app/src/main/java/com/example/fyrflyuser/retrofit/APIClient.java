package com.example.fyrflyuser.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

 public  class APIClient {

    private static Retrofit retrofit = null;
    private static String Base_URL = "https://evasofts.com/fyrfly/";

  public  static Retrofit getRetrofitClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
      Gson gson = new GsonBuilder()
              .setLenient()
              .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();


        return retrofit;
    }

}
