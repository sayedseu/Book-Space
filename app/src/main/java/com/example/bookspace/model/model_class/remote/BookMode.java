package com.example.bookspace.model.model_class.remote;

import com.google.gson.annotations.SerializedName;

public class BookMode {
    @SerializedName("bookMode")
    private String bookMode;
    @SerializedName("modeId")
    private long modeId;

    public String getBookMode() {
        return bookMode;
    }

    public void setBookMode(String bookMode) {
        this.bookMode = bookMode;
    }

    public long getModeId() {
        return modeId;
    }

    public void setModeId(long modeId) {
        this.modeId = modeId;
    }

}
