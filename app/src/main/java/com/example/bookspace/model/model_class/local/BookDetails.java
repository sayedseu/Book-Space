package com.example.bookspace.model.model_class.local;

import androidx.room.Embedded;
import androidx.room.Relation;

public class BookDetails {
    @Embedded
    public BookInfo bookInfo;
    @Relation(
            parentColumn = "bookId",
            entityColumn = "bookInfoId"
    )
    public BookCategory bookCategory;
    @Relation(
            parentColumn = "bookId",
            entityColumn = "bookInfoId"
    )
    public BookMode bookMode;
    @Relation(
            parentColumn = "bookId",
            entityColumn = "bookIfoId"
    )
    public ExpectedBook expectedBook;

    public BookDetails(BookInfo bookInfo, BookCategory bookCategory, BookMode bookMode, ExpectedBook expectedBook) {
        this.bookInfo = bookInfo;
        this.bookCategory = bookCategory;
        this.bookMode = bookMode;
        this.expectedBook = expectedBook;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
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
}
