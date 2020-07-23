package com.example.bookspace.app;

import android.content.Context;

import com.example.bookspace.model.local.db.AppRoomDatabase;
import com.example.bookspace.model.local.source.BookDataSource;
import com.example.bookspace.model.local.source.LocalBookDataSource;
import com.example.bookspace.model.remote.ApiInterface;
import com.example.bookspace.model.remote.RetrofitApiClient;

import retrofit2.Retrofit;

public class Injection {

    private static BookDataSource provideBookDataSource(Context context) {
        AppRoomDatabase database = AppRoomDatabase.getRoomDatabase(context);
        return new LocalBookDataSource(database.getAppDaoInstance());
    }

    private static ApiInterface provideApiInterface(Context context) {
        Retrofit retrofitApiClient = RetrofitApiClient.getClient(context);
        return retrofitApiClient.create(ApiInterface.class);
    }

    public static ViewModelProviderFactory provideViewModelProviderFactory(Context context) {
        BookDataSource bookDataSource = provideBookDataSource(context);
        ApiInterface apiInterface = provideApiInterface(context);
        return new ViewModelProviderFactory(bookDataSource, apiInterface);
    }
}
