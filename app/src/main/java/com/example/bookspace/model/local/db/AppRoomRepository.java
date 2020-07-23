package com.example.bookspace.model.local.db;

import android.content.Context;

import com.example.bookspace.model.model_class.local.BookCategory;
import com.example.bookspace.model.model_class.local.BookDetails;
import com.example.bookspace.model.model_class.local.BookInfo;
import com.example.bookspace.model.model_class.local.BookMode;
import com.example.bookspace.model.model_class.local.ExpectedBook;
import com.example.bookspace.model.model_class.local.User;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class AppRoomRepository {
    private static volatile AppRoomRepository appRoomRepository;
    private String TAG = "sayed";
    private AppDao appDao;
    private ExecutorService service;

    private AppRoomRepository(Context context) {
        appDao = AppRoomDatabase.getRoomDatabase(context).getAppDaoInstance();
        service = AppRoomDatabase.databaseWriteExecutor;
    }

    public static AppRoomRepository getInstance(Context context) {
        if (appRoomRepository == null) {
            synchronized (AppRoomRepository.class) {
                if (appRoomRepository == null) {
                    appRoomRepository = new AppRoomRepository(context);
                }
            }
        }
        return appRoomRepository;
    }

    public long insert(BookInfo bookInfo) {
        Future<Long> future = service.submit(new BookInfoCallable(bookInfo));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long insert(BookMode bookMode) {
        Future<Long> future = service.submit(new BookModeCallable(bookMode));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long insert(BookCategory bookCategory) {
        Future<Long> future = service.submit(new BookCategoryCallable(bookCategory));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long insert(ExpectedBook expectedBook) {
        Future<Long> future = service.submit(new ExpectedBookCallable(expectedBook));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long insert(User user) {
        Future<Long> future = service.submit(new UserInsertCallable(user));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int update(User user) {
        Future<Integer> future = service.submit(new UserUpdateCallable(user));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public User[] checkNumber(String number) {
        Future<User[]> future = service.submit(new CheckNumberCallable(number));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User[] authenticate(String number, String password) {
        Future<User[]> future = service.submit(new AuthenticateCallable(number, password));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User[] findUserById(long id) {
        Future<User[]> future = service.submit(new FindUserByIdCallable(id));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BookDetails> getBookList() {
        Future<List<BookDetails>> future = service.submit(new RetrieveBookDetailsCallable());
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class BookInfoCallable implements Callable<Long> {
        private BookInfo bookInfo;

        BookInfoCallable(BookInfo bookInfo) {
            this.bookInfo = bookInfo;
        }

        @Override
        public Long call() throws Exception {
            return appDao.insert(bookInfo);
        }
    }

    private class BookModeCallable implements Callable<Long> {
        private BookMode bookMode;

        BookModeCallable(BookMode bookMode) {
            this.bookMode = bookMode;
        }

        @Override
        public Long call() throws Exception {
            return appDao.insert(bookMode);
        }
    }

    private class BookCategoryCallable implements Callable<Long> {
        private BookCategory bookCategory;

        BookCategoryCallable(BookCategory bookCategory) {
            this.bookCategory = bookCategory;
        }

        @Override
        public Long call() throws Exception {
            return appDao.insert(bookCategory);
        }
    }

    private class ExpectedBookCallable implements Callable<Long> {
        private ExpectedBook expectedBook;

        ExpectedBookCallable(ExpectedBook expectedBook) {
            this.expectedBook = expectedBook;
        }

        @Override
        public Long call() throws Exception {
            return appDao.insert(expectedBook);
        }
    }

    private class UserInsertCallable implements Callable<Long> {
        private User user;

        public UserInsertCallable(User user) {
            this.user = user;
        }

        @Override
        public Long call() throws Exception {
            return appDao.insert(user);
        }
    }

    private class UserUpdateCallable implements Callable<Integer> {
        private User user;

        public UserUpdateCallable(User user) {
            this.user = user;
        }

        @Override
        public Integer call() throws Exception {
            return appDao.update(user);
        }
    }

    private class AuthenticateCallable implements Callable<User[]> {
        private String number;
        private String password;

        public AuthenticateCallable(String number, String password) {
            this.number = number;
            this.password = password;
        }

        @Override
        public User[] call() throws Exception {
            return appDao.authenticate(number, password);
        }
    }

    private class CheckNumberCallable implements Callable<User[]> {
        private String number;

        public CheckNumberCallable(String number) {
            this.number = number;
        }

        @Override
        public User[] call() throws Exception {
            return appDao.checkNumber(number);
        }
    }

    private class RetrieveBookDetailsCallable implements Callable<List<BookDetails>> {

        @Override
        public List<BookDetails> call() throws Exception {
            return appDao.getBookDetails();
        }
    }

    private class FindUserByIdCallable implements Callable<User[]> {
        private long id;

        public FindUserByIdCallable(long id) {
            this.id = id;
        }

        @Override
        public User[] call() throws Exception {
            return appDao.userById(id);
        }
    }

}
