package com.example.bookspace.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookspace.model.model_class.remote.BookDetails;
import com.example.bookspace.model.model_class.remote.User;


public class SharedViewModel extends ViewModel {
    private final MutableLiveData<BookDetails> uploadingBook = new MutableLiveData<>();
    private final MutableLiveData<BookDetails> currentViewedBook = new MutableLiveData<>();
    private final MutableLiveData<User> registeringUser = new MutableLiveData<>();

    public void setUploadingBook(BookDetails bookDetails) {
        uploadingBook.setValue(bookDetails);
    }

    public void setCurrentViewedBook(BookDetails bookDetails) {
        currentViewedBook.setValue(bookDetails);
    }

    public void setRegisteringUser(User user) {
        registeringUser.setValue(user);
    }

    public LiveData<BookDetails> observeUploadingBook() {
        return uploadingBook;
    }

    public LiveData<User> observeRegisteringUser() {
        return registeringUser;
    }

    public LiveData<BookDetails> observeCurrentViewedBook() {
        return currentViewedBook;
    }
}
