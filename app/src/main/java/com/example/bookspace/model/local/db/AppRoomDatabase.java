package com.example.bookspace.model.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bookspace.model.model_class.local.BookCategory;
import com.example.bookspace.model.model_class.local.BookInfo;
import com.example.bookspace.model.model_class.local.BookMode;
import com.example.bookspace.model.model_class.local.ExpectedBook;
import com.example.bookspace.model.model_class.local.Interested;
import com.example.bookspace.model.model_class.local.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {BookInfo.class, BookMode.class, BookCategory.class, ExpectedBook.class, User.class, Interested.class}, version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
    private static volatile AppRoomDatabase INSTANCE;

    public static AppRoomDatabase getRoomDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "book_store")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract AppDao getAppDaoInstance();
}
