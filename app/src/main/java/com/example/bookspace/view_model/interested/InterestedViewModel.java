package com.example.bookspace.view_model.interested;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookspace.model.model_class.remote.Interested;
import com.example.bookspace.model.remote.ApiInterface;
import com.example.bookspace.model.remote.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class InterestedViewModel extends ViewModel {
    private static final String TAG = "sayed";
    private ApiInterface apiInterface;
    private MediatorLiveData<Resource<List<Interested>>> interestedBookList = new MediatorLiveData<>();
    private MediatorLiveData<Resource<Boolean>> deletingResult = new MediatorLiveData<>();

    public InterestedViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public LiveData<Resource<List<Interested>>> getInterestedList(long userid) {
        interestedBookList.setValue(Resource.loading(null));
        LiveData<Resource<List<Interested>>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.getInterestedBook(userid)
                        .onErrorReturn(throwable -> {
                            List<Interested> errorList = new ArrayList<>();
                            Interested errorInterested = new Interested();
                            errorInterested.setId(-1);
                            errorList.add(errorInterested);
                            return errorList;
                        })
                        .map((Function<List<Interested>, Resource<List<Interested>>>) interesteds -> {
                            if (interesteds != null) {
                                if (!interesteds.isEmpty()) {
                                    if (interesteds.get(0).getId() == -1) {
                                        return Resource.error("", null);
                                    }
                                }
                                return Resource.success(interesteds);
                            } else return Resource.error("", null);
                        }).subscribeOn(Schedulers.io()));
        interestedBookList.addSource(source, listResource -> {
            interestedBookList.setValue(listResource);
            interestedBookList.removeSource(source);
        });
        return interestedBookList;
    }

    public void deleteInterestedBook(long id) {
        deletingResult.setValue(Resource.loading(null));
        LiveData<Resource<Boolean>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.deleteInterestedBook(id)
                        .onErrorReturn(throwable -> {
                            Log.i(TAG, "deleteInterestedBook: " + throwable.toString());
                            return -1;
                        })
                        .map((Function<Integer, Resource<Boolean>>) integer -> {
                            if (integer == 1) return Resource.success(true);
                            else return Resource.error("", null);
                        }).subscribeOn(Schedulers.io()));
        deletingResult.addSource(source, resource -> {
            deletingResult.setValue(resource);
            deletingResult.removeSource(source);
        });
    }

    public LiveData<Resource<Boolean>> observerDeleting() {
        return deletingResult;
    }

    public void clearObserver() {
        deletingResult.setValue(null);
    }
}
