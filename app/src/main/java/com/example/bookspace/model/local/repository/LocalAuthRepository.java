package com.example.bookspace.model.local.repository;

import com.example.bookspace.custom.RequestCompleteListener;
import com.example.bookspace.model.model_class.local.User;

public interface LocalAuthRepository {
    void authenticate(String number, String password, RequestCompleteListener<User> completeListener);

    void register(User user, RequestCompleteListener<Long> completeListener);

    void update(User updatedUser, RequestCompleteListener<Integer> completeListener);

    void checkNumber(String number, RequestCompleteListener<User> completeListener);

    void findUserById(long id, RequestCompleteListener<User> completeListener);
}
