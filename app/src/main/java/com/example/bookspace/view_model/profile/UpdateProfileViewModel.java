package com.example.bookspace.view_model.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookspace.model.model_class.remote.User;
import com.example.bookspace.model.remote.ApiInterface;
import com.example.bookspace.model.remote.Resource;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UpdateProfileViewModel extends ViewModel {
    private ApiInterface apiInterface;
    private MediatorLiveData<Resource<User>> updatingProfileResult = new MediatorLiveData<>();
    private MediatorLiveData<Resource<User>> updatingPasswordResult = new MediatorLiveData<>();

    public UpdateProfileViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void updateProfile(User user) {
        updatingProfileResult.setValue(Resource.loading(null));
        LiveData<Resource<User>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.updateProfile(user)
                        .onErrorReturn(throwable -> {
                            User errorUser = new User();
                            errorUser.setUserId(-1);
                            return errorUser;
                        })
                        .map((Function<User, Resource<User>>) result -> {
                            if (result.getUserId() == -1) return Resource.error("", null);
                            return Resource.success(result);
                        })
                        .subscribeOn(Schedulers.io()));
        updatingProfileResult.addSource(source, userResource -> {
            updatingProfileResult.setValue(userResource);
            updatingProfileResult.removeSource(source);
        });
    }

    public void updatePassword(String number, String newPassword) {
        updatingPasswordResult.setValue(Resource.loading(null));
        LiveData<Resource<User>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.updatePassword(number, newPassword)
                        .onErrorReturn(throwable -> {
                            User errorUser = new User();
                            errorUser.setUserId(-1);
                            return errorUser;
                        })
                        .map((Function<User, Resource<User>>) result -> {
                            if (result.getUserId() == -1) return Resource.error("", null);
                            return Resource.success(result);
                        })
                        .subscribeOn(Schedulers.io()));
        updatingPasswordResult.addSource(source, userResource -> {
            updatingPasswordResult.setValue(userResource);
            updatingPasswordResult.removeSource(source);
        });
    }

    public LiveData<Resource<User>> observerProfileUpdating() {
        return updatingProfileResult;
    }

    public LiveData<Resource<User>> observerPasswordUpdating() {
        return updatingPasswordResult;
    }

    public void clearObserver() {
        updatingProfileResult.setValue(null);
        updatingPasswordResult.setValue(null);
    }
}
