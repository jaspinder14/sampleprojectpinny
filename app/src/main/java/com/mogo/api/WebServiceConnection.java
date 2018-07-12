package com.mogo.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mobile on 7/31/2017.
 */

public class WebServiceConnection {
    public static final String baseurl = "http://live.csdevhub.com/mogoapp/ws/";
    public static WebServiceHolder holder;

    public WebServiceConnection() {
        init();
    }

    public void init() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        holder = retrofit.create(WebServiceHolder.class);
    }

    public static WebServiceConnection getInstance() {
        return new WebServiceConnection();
    }
}