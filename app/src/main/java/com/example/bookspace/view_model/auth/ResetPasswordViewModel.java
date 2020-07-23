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

public class ResetPasswordViewModel extends ViewModel {
    private ApiInterface apiInterface;
    private MediatorLiveData<Resource<Boolean>> resetPasswordResult = new MediatorLiveData<>();

    public ResetPasswordViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void resetPassword(String number, String newPassword) {
        resetPasswordResult.setValue(Resource.loading(null));
        LiveData<Resource<Boolean>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.updatePassword(number, newPassword)
                        .onErrorReturn(throwable -> {
                            User user = new User();
                            user.setUserId(-1);
                            return user;
                        })
                        .map((Function<User, Resource<Boolean>>) user -> {
                            if (user.getUserId() == -1) {
                                return Resource.error("", null);
                            }
                            return Resource.success(true);
                        }).subscribeOn(Schedulers.io()));
        resetPasswordResult.addSource(source, resource -> {
            resetPasswordResult.setValue(resource);
            resetPasswordResult.removeSource(source);
        });
    }

    public LiveData<Resource<Boolean>> observeResetPasswordResult() {
        return resetPasswordResult;
    }

    public void clearObserver() {
        resetPasswordResult.setValue(null);
    }
}
