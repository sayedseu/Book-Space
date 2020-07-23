package com.example.bookspace.model.local.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.bookspace.model.model_class.local.BookCategory;
import com.example.bookspace.model.model_class.local.BookDetails;
import com.example.bookspace.model.model_class.local.BookInfo;
import com.example.bookspace.model.model_class.local.BookMode;
import com.example.bookspace.model.model_class.local.ExpectedBook;
import com.example.bookspace.model.model_class.local.Interested;
import com.example.bookspace.model.model_class.local.InterestedWithUser;
import com.example.bookspace.model.model_class.local.User;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface AppDao {
    @Insert
    long insert(BookInfo bookInfo);

    @Insert
    long insert(BookCategory bookCategory);

    @Insert
    long insert(BookMode bookMode);

    @Insert
    long insert(ExpectedBook expectedBook);

    @Insert
    long insert(User user);

    @Insert
    Single<Long> insert(Interested interested);

    @Update
    int update(User user);

    @Delete
    Single<Integer> delete(Interested interested);

    @Transaction
    @Query("SELECT * FROM BOOKINFO")
    List<BookDetails> getBookDetails();

    @Transaction
    @Query("SELECT * FROM BOOKINFO")
    Flowable<List<BookDetails>> retrieveBookDetailsList();

    @Query("SELECT * FROM users WHERE phoneNumber like :number AND password like :password")
    User[] authenticate(String number, String password);

    @Query("SELECT * FROM users WHERE phoneNumber like :number")
    User[] checkNumber(String number);

    @Query("SELECT * FROM users WHERE userId like :id")
    User[] userById(long id);

    @Query("SELECT * FROM interested WHERE userId like :userId AND bookId like :bookId")
    Maybe<Interested[]> findInterestedByUserAndBookId(long userId, long bookId);

    @Transaction
    @Query("SELECT * FROM interested WHERE userId like :id")
    Flowable<List<InterestedWithUser>> getInterestedWithUserById(long id);

}
