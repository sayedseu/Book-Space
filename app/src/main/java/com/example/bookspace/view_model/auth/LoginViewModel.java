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

public class LoginViewModel extends ViewModel {
    private static final String TAG = "sayed";
    private ApiInterface apiInterface;
    private MediatorLiveData<Resource<User>> authenticateResult = new MediatorLiveData<>();

    public LoginViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void authenticate(String username, String password) {
        authenticateResult.setValue(Resource.loading(null));
        LiveData<Resource<User>> source = LiveDataReactiveStreams.fromPublisher(
                apiInterface.authenticate(username, password)
                        .onErrorReturn(throwable -> {
                            User user = new User();
                            user.setUserId(-1);
                            return user;
                        })
                        .map((Function<User, Resource<User>>) user -> {
                            if (user.getUserId() == -1) return Resource.error("", null);
                            return Resource.success(user);
                        })
                        .subscribeOn(Schedulers.io()));
        authenticateResult.addSource(source, userResource -> {
            authenticateResult.setValue(userResource);
            authenticateResult.removeSource(source);
        });
    }

    public LiveData<Resource<User>> observeAuthenticateResult() {
        return authenticateResult;
    }

    public void clearObserver() {
        authenticateResult.setValue(null);
    }
}
