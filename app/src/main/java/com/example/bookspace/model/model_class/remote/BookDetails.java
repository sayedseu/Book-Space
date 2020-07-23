package com.example.bookspace.model.model_class.remote;

import com.google.gson.annotations.SerializedName;

public class BookDetails {
    @SerializedName("bookCategory")
    private BookCategory bookCategory;
    @SerializedName("bookInfo")
    private BookInfo bookInfo;
    @SerializedName("bookMode")
    private BookMode bookMode;
    @SerializedName("expectedBook")
    private ExpectedBook expectedBook;
    @SerializedName("id")
    private long id;
    @SerializedName("owner")
    private User owner;

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public BookMode getBookMode() {
        return bookMode;
    }

    public void setBookMode(BookMode bookMode) {
        this.bookMode = bookMode;
    }

    public ExpectedBook getExpectedBook() {
        return expectedBook;
    }

    public void setExpectedBook(ExpectedBook expectedBook) {
        this.expectedBook = expectedBook;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

}
