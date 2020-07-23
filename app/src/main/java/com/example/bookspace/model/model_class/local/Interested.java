package com.example.bookspace.model.model_class.local;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Interested {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long userId;
    private long bookId;
    private long modeId;
    private long categoryId;
    private long expectedBookId;

    public Interested(long id, long userId, long bookId, long modeId, long categoryId, long expectedBookId) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.modeId = modeId;
        this.categoryId = categoryId;
        this.expectedBookId = expectedBookId;
    }

    @Ignore
    public Interested(long userId, long bookId, long modeId, long categoryId, long expectedBookId) {
        this.userId = userId;
        this.bookId = bookId;
        this.modeId = modeId;
        this.categoryId = categoryId;
        this.expectedBookId = expectedBookId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getModeId() {
        return modeId;
    }

    public void setModeId(long modeId) {
        this.modeId = modeId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getExpectedBookId() {
        return expectedBookId;
    }

    public void setExpectedBookId(long expectedBookId) {
        this.expectedBookId = expectedBookId;
    }

    @Override
    public String toString() {
        return "Interested{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", modeId=" + modeId +
                ", categoryId=" + categoryId +
                ", expectedBookId=" + expectedBookId +
                '}';
    }
}
