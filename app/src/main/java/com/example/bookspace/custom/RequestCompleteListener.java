package com.example.bookspace.custom;

public interface RequestCompleteListener<T> {
    void onRequestSuccess(T data);

    void onRequestFailed(String errorMessage);
}
