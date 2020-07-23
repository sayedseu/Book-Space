package com.example.bookspace.view_model.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookspace.model.remote.ApiInterface;
import com.example.bookspace.model.remote.Resource;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SignupViewModel extends ViewModel {
    private ApiInterface apiInterface;
    private MediatorLiveData<Resource<Boolean>> observeSendOtpResult = new MediatorLiveData<>();
    private MediatorLiveData<Resource<Boolean>> checkDuplicateUserResult = new MediatorLiveData<>();

    public SignupViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void sendOtp(String number) {
        observeSendOtpResult.setValue(Resource.loading(null));
        LiveData<Resource<Boolean>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.sendOtp(number)
                        .onErrorReturn(throwable -> 404)
                        .map((Function<Integer, Resource<Boolean>>) result -> {
                            if (result == 200) return Resource.success(true);
                            return Resource.error("Something wrong", null);
                        })
                        .subscribeOn(Schedulers.io()));
        observeSendOtpResult.addSource(source, booleanResource -> {
            observeSendOtpResult.setValue(booleanResource);
            observeSendOtpResult.removeSource(source);
        });
    }

    public void checkDuplicateUser(String number, String email) {
        checkDuplicateUserResult.setValue(Resource.loading(null));
        LiveData<Resource<Boolean>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.checkDuplicateUser(number, email)
                        .onErrorReturn(throwable -> 404)
                        .map((Function<Integer, Resource<Boolean>>) result -> {
                            if (result == 200) return Resource.success(true);
                            else if (result == 409) return Resource.success(false);
                            return Resource.error("Something wrong", null);
                        })
                        .subscribeOn(Schedulers.io()));
        checkDuplicateUserResult.addSource(source, booleanResource -> {
            checkDuplicateUserResult.setValue(booleanResource);
            checkDuplicateUserResult.removeSource(source);
        });
    }

    public LiveData<Resource<Boolean>> observeSendOtpResult() {
        return observeSendOtpResult;
    }

    public LiveData<Resource<Boolean>> observeCheckDuplicateUser() {
        return checkDuplicateUserResult;
    }

    public void clearObserver() {
        observeSendOtpResult.setValue(null);
        checkDuplicateUserResult.setValue(null);
    }
}
