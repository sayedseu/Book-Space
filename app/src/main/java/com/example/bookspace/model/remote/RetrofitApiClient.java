package com.example.bookspace.model.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {

    private static final String BASE_URL = "http://192.168.43.235:8083/api/v1/";
    private static Retrofit retrofit = null;
    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private static OkHttpClient okHttpClient = new OkHttpClient()
            .newBuilder()
            .addInterceptor(chain -> {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest
                        .newBuilder()
                        .addHeader("Authorization", Credentials.basic("teamjupiter007", "teamjupiter007"));
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }).build();

    private RetrofitApiClient() {
    }

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            synchronized (RetrofitApiClient.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }

        return retrofit;
    }
}
