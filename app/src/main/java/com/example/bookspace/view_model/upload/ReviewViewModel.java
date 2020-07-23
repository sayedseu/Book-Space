package com.example.bookspace.view_model.upload;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookspace.model.model_class.remote.BookDetails;
import com.example.bookspace.model.remote.ApiInterface;
import com.example.bookspace.model.remote.Resource;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ReviewViewModel extends ViewModel {
    private ApiInterface apiInterface;
    private MediatorLiveData<Resource<Boolean>> uploadingResult = new MediatorLiveData<>();

    public ReviewViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void uploadBook(BookDetails bookDetails) {
        uploadingResult.setValue(Resource.loading(null));
        LiveData<Resource<Boolean>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.insertBook(bookDetails)
                        .onErrorReturn(throwable -> -1L)
                        .map((Function<Long, Resource<Boolean>>) aLong -> {
                            if (aLong >= 1) return Resource.success(true);
                            else return Resource.error("", null);
                        }).subscribeOn(Schedulers.io()));
        uploadingResult.addSource(source, booleanResource -> {
            uploadingResult.setValue(booleanResource);
            uploadingResult.removeSource(source);
        });
    }

    public LiveData<Resource<Boolean>> observeUploadingResult() {
        return uploadingResult;
    }

    public void clearObserver() {
        uploadingResult.setValue(null);
    }
}
