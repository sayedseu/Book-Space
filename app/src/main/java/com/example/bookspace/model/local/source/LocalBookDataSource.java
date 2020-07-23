package com.example.bookspace.model.local.source;

import com.example.bookspace.model.local.db.AppDao;
import com.example.bookspace.model.model_class.local.BookDetails;
import com.example.bookspace.model.model_class.local.Interested;
import com.example.bookspace.model.model_class.local.InterestedWithUser;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class LocalBookDataSource implements BookDataSource {
    private AppDao appDao;

    public LocalBookDataSource(AppDao appDao) {
        this.appDao = appDao;
    }

    @Override
    public Single<Long> insert(Interested interested) {
        return appDao.insert(interested);
    }

    @Override
    public Single<Integer> delete(Interested interested) {
        return appDao.delete(interested);
    }

    @Override
    public Maybe<Interested[]> find(long userId, long bookInfoId) {
        return appDao.findInterestedByUserAndBookId(userId, bookInfoId);
    }

    @Override
    public Flowable<List<InterestedWithUser>> retrieve(long userId) {
        return appDao.getInterestedWithUserById(userId);
    }

    @Override
    public Flowable<List<BookDetails>> retrieve() {
        return appDao.retrieveBookDetailsList();
    }

}
