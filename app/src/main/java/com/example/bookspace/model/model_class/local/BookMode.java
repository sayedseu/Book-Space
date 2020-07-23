package com.example.bookspace.model.model_class.local;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class BookMode {
    @PrimaryKey(autoGenerate = true)
    private long modeId;
    private long bookInfoId;
    private String bookMode;


    public BookMode(long modeId, long bookInfoId, String bookMode) {
        this.modeId = modeId;
        this.bookInfoId = bookInfoId;
        this.bookMode = bookMode;
    }

    @Ignore
    public BookMode(long bookInfoId, String bookMode) {
        this.bookInfoId = bookInfoId;
        this.bookMode = bookMode;
    }

    @Ignore
    public BookMode(String bookMode) {
        this.bookMode = bookMode;
    }

    public long getModeId() {
        return modeId;
    }

    public void setModeId(long modeId) {
        this.modeId = modeId;
    }

    public long getBookInfoId() {
        return bookInfoId;
    }

    public void setBookInfoId(long bookInfoId) {
        this.bookInfoId = bookInfoId;
    }

    public String getBookMode() {
        return bookMode;
    }

    public void setBookMode(String bookMode) {
        this.bookMode = bookMode;
    }
}
