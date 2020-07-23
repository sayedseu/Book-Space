package com.example.bookspace.model.local.repository;

import com.example.bookspace.custom.RequestCompleteListener;
import com.example.bookspace.model.model_class.local.BookDetails;

import java.util.List;

public interface LocalDataRepository {
    void getBookList(RequestCompleteListener<List<BookDetails>> completeListener);

    void insert(BookDetails bookDetails, RequestCompleteListener<Boolean> completeListener);
}
