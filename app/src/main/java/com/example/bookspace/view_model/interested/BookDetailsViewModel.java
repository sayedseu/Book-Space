package com.example.bookspace.view_model.interested;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookspace.model.model_class.remote.Interested;
import com.example.bookspace.model.remote.ApiInterface;
import com.example.bookspace.model.remote.Resource;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class BookDetailsViewModel extends ViewModel {
    private ApiInterface apiInterface;
    private MediatorLiveData<Resource<Boolean>> insertedResult = new MediatorLiveData<>();

    public BookDetailsViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void insertInterestedBook(Interested interested) {
        insertedResult.setValue(Resource.loading(null));
        LiveData<Resource<Boolean>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.insertInterestedBook(interested)
                        .onErrorReturn(throwable -> -404L)
                        .map((Function<Long, Resource<Boolean>>) result -> {
                            Log.i("sayed", "map: " + result);
                            if (result > 0) return Resource.success(true);
                            if (result == -1) return Resource.success(false);
                            return Resource.error("", null);
                        }).subscribeOn(Schedulers.io()));
        insertedResult.addSource(source, resource -> {
            insertedResult.setValue(resource);
            insertedResult.removeSource(source);
        });
    }

    public LiveData<Resource<Boolean>> observeInserting() {
        return insertedResult;
    }

    public void clearObserver() {
        insertedResult.setValue(null);
    }
}
