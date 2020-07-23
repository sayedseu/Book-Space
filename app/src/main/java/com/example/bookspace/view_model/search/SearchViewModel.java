package com.example.bookspace.view_model.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookspace.model.model_class.remote.BookDetails;
import com.example.bookspace.model.remote.ApiInterface;
import com.example.bookspace.model.remote.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {
    private ApiInterface apiInterface;
    private MediatorLiveData<Resource<List<BookDetails>>> bookList;

    public SearchViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public LiveData<Resource<List<BookDetails>>> retrieve() {
        if (bookList == null) {
            bookList = new MediatorLiveData<>();
            bookList.setValue(Resource.loading(null));
            LiveData<Resource<List<BookDetails>>> source = LiveDataReactiveStreams.fromPublisher(
                    apiInterface.getAllBook()
                            .onErrorReturn(throwable -> {
                                Log.i("sayed", "retrieve: " + throwable.toString());
                                List<BookDetails> bookDetailsList = new ArrayList<>();
                                BookDetails bookDetails = new BookDetails();
                                bookDetails.setId(-1);
                                bookDetailsList.add(bookDetails);
                                return bookDetailsList;
                            })
                            .map((Function<List<BookDetails>, Resource<List<BookDetails>>>) bookDetails -> {
                                if (bookDetails != null) {
                                    if (!bookDetails.isEmpty()) {
                                        if (bookDetails.get(0).getId() == -1) {
                                            return Resource.error("", null);
                                        }
                                    }
                                    return Resource.success(bookDetails);
                                } else return Resource.error("", null);
                            }).subscribeOn(Schedulers.io()));
            bookList.addSource(source, resource -> {
                bookList.setValue(resource);
                bookList.removeSource(source);
            });
        }
        return bookList;
    }
}
