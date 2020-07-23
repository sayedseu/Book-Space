package com.example.bookspace.view_model.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookspace.model.model_class.remote.User;
import com.example.bookspace.model.remote.ApiInterface;
import com.example.bookspace.model.remote.Resource;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class OtpViewModel extends ViewModel {
    private static final String TAG = "sayed";
    private ApiInterface apiInterface;
    private MediatorLiveData<Resource<Long>> registerResult = new MediatorLiveData<>();
    private MediatorLiveData<Resource<Boolean>> verifyOtpResult = new MediatorLiveData<>();
    private MediatorLiveData<Resource<Boolean>> sendOtpResult = new MediatorLiveData<>();

    public OtpViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void registerUser(User user) {
        registerResult.setValue(Resource.loading(null));
        LiveData<Resource<Long>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.registerUser(user)
                        .onErrorReturn(throwable -> 404L)
                        .map((Function<Long, Resource<Long>>) result -> {
                            if (result >= 1) return Resource.success(result);
                            return Resource.error("Something wrong", null);
                        })
                        .subscribeOn(Schedulers.io()));
        registerResult.addSource(source, longResource -> {
            registerResult.setValue(longResource);
            registerResult.removeSource(source);
        });
    }

    public void sendOtp(String number) {
        sendOtpResult.setValue(Resource.loading(null));
        LiveData<Resource<Boolean>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.sendOtp(number)
                        .onErrorReturn(throwable -> 404)
                        .map((Function<Integer, Resource<Boolean>>) result -> {
                            if (result == 200) return Resource.success(true);
                            return Resource.error("Something wrong", null);
                        })
                        .subscribeOn(Schedulers.io()));
        sendOtpResult.addSource(source, booleanResource -> {
            sendOtpResult.setValue(booleanResource);
            sendOtpResult.removeSource(source);
        });
    }


    public void verifyOtp(String number, String code) {
        verifyOtpResult.setValue(Resource.loading(null));
        LiveData<Resource<Boolean>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.verifyOtp(number, code)
                        .onErrorReturn(throwable -> 400)
                        .map((Function<Integer, Resource<Boolean>>) result -> {
                            if (result == 200) return Resource.success(true);
                            else if (result == 500) return Resource.success(false);
                            return Resource.error("Something wrong", null);
                        }).subscribeOn(Schedulers.io()));
        verifyOtpResult.addSource(source, booleanResource -> {
            verifyOtpResult.setValue(booleanResource);
            verifyOtpResult.removeSource(source);
        });
    }

    public LiveData<Resource<Boolean>> observeVerifyOtp() {
        return verifyOtpResult;
    }

    public LiveData<Resource<Boolean>> observeSendOtp() {
        return sendOtpResult;
    }

    public LiveData<Resource<Long>> observeRegisterResult() {
        return registerResult;
    }

    public void clearObserver() {
        verifyOtpResult.setValue(null);
        sendOtpResult.setValue(null);
        registerResult.setValue(null);
    }
}
