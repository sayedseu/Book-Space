package com.example.bookspace.model.local.repository;

import android.content.Context;

import com.example.bookspace.custom.RequestCompleteListener;
import com.example.bookspace.model.local.db.AppRoomRepository;
import com.example.bookspace.model.model_class.local.BookCategory;
import com.example.bookspace.model.model_class.local.BookDetails;
import com.example.bookspace.model.model_class.local.BookInfo;
import com.example.bookspace.model.model_class.local.BookMode;
import com.example.bookspace.model.model_class.local.ExpectedBook;

import java.util.List;

public class LocalDataRepositoryImpl implements LocalDataRepository {
    private static volatile LocalDataRepositoryImpl instance;
    private AppRoomRepository repository;

    private LocalDataRepositoryImpl(Context context) {
        repository = AppRoomRepository.getInstance(context);
    }

    public static LocalDataRepositoryImpl getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalAuthRepositoryImpl.class) {
                if (instance == null) {
                    instance = new LocalDataRepositoryImpl(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void getBookList(RequestCompleteListener<List<BookDetails>> completeListener) {
        List<BookDetails> bookDetails = repository.getBookList();
        if (bookDetails != null) {
            completeListener.onRequestSuccess(bookDetails);
        } else {
            completeListener.onRequestFailed("Nothing found");
        }
    }

    @Override
    public void insert(BookDetails bookDetails, RequestCompleteListener<Boolean> completeListener) {
        BookInfo bookInfo = bookDetails.getBookInfo();
        BookCategory bookCategory = bookDetails.getBookCategory();
        BookMode bookMode = bookDetails.getBookMode();
        ExpectedBook expectedBook = bookDetails.getExpectedBook();

        long bookInfoId = repository.insert(bookInfo);

        if (bookInfoId > 0) {
            bookCategory.setBookInfoId(bookInfoId);
            bookMode.setBookInfoId(bookInfoId);
            expectedBook.setBookIfoId(bookInfoId);
            long categoryId = repository.insert(bookCategory);
            long bookModeId = repository.insert(bookMode);
            long expectedBookId = repository.insert(expectedBook);
            if (categoryId > 0 && bookModeId > 0 && expectedBookId > 0) {
                completeListener.onRequestSuccess(true);
            } else {
                completeListener.onRequestFailed("");
            }
        } else {
            completeListener.onRequestFailed("");
        }
    }
}
