package com.example.bookspace.model.local.repository;

import android.content.Context;

import com.example.bookspace.custom.RequestCompleteListener;
import com.example.bookspace.model.local.db.AppRoomRepository;
import com.example.bookspace.model.model_class.local.User;

public class LocalAuthRepositoryImpl implements LocalAuthRepository {

    private static volatile LocalAuthRepositoryImpl instance;
    private AppRoomRepository repository;

    private LocalAuthRepositoryImpl(Context context) {
        repository = AppRoomRepository.getInstance(context);
    }

    public static LocalAuthRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalAuthRepositoryImpl.class) {
                if (instance == null) {
                    instance = new LocalAuthRepositoryImpl(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void authenticate(String number, String password, RequestCompleteListener<User> completeListener) {
        User[] authenticate = repository.authenticate(number, password);
        if (authenticate != null && authenticate.length > 0) {
            completeListener.onRequestSuccess(authenticate[0]);
        } else {
            completeListener.onRequestFailed("");
        }
    }

    @Override
    public void register(User user, RequestCompleteListener<Long> completeListener) {
        long insert = repository.insert(user);
        if (insert > 0) {
            completeListener.onRequestSuccess(insert);
        } else {
            completeListener.onRequestFailed("");
        }

    }

    @Override
    public void update(User updatedUser, RequestCompleteListener<Integer> completeListener) {
        int update = repository.update(updatedUser);
        if (update > 0) {
            completeListener.onRequestSuccess(update);
        } else {
            completeListener.onRequestFailed("");
        }
    }

    @Override
    public void checkNumber(String number, RequestCompleteListener<User> completeListener) {
        User[] users = repository.checkNumber(number);
        if (users != null && users.length > 0) {
            completeListener.onRequestSuccess(users[0]);
        } else {
            completeListener.onRequestSuccess(null);
        }
    }

    @Override
    public void findUserById(long id, RequestCompleteListener<User> completeListener) {
        User[] userById = repository.findUserById(id);
        if (userById != null && userById.length > 0) {
            completeListener.onRequestSuccess(userById[0]);
        } else {
            completeListener.onRequestFailed("");
        }
    }
}
