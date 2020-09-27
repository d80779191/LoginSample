package com.example.loginexample.base;

import android.content.Context;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static Context context;
    private static OkHttpClient okHttpClient;

    public static Retrofit getInstance(){
        if (retrofit == null) {
            synchronized (ApiHelper.class) {
                if (retrofit == null) {
                    new RetrofitClient(context);
                }
            }
        }
        return retrofit;
    }

    private RetrofitClient(Context context){
        this.context = context;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseUtils.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .callbackExecutor(Executors.newFixedThreadPool(5))
                .build();
    }
}
