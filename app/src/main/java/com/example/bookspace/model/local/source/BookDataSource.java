package com.example.bookspace.model.local.source;

import com.example.bookspace.model.model_class.local.BookDetails;
import com.example.bookspace.model.model_class.local.Interested;
import com.example.bookspace.model.model_class.local.InterestedWithUser;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface BookDataSource {
    Single<Long> insert(Interested interested);

    Single<Integer> delete(Interested interested);

    Maybe<Interested[]> find(long userId, long bookInfoId);

    Flowable<List<InterestedWithUser>> retrieve(long userId);

    Flowable<List<BookDetails>> retrieve();
}
