package com.example.bookspace.model.model_class.local;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class BookInfo {
    @PrimaryKey(autoGenerate = true)
    private long bookId;
    private long userId;
    private String bookName;
    private String bookAuthorName;
    private String bookPrice;
    private String bookPublicationYear;
    private String bookEdition;
    private String bookLocation;
    private String bookCondition;
    private String isbnNumber;
    private String imageLocation1;
    private String imageLocation2;
    private String imageLocation3;
    private String dateAndTime;

    public BookInfo(long bookId, long userId, String bookName, String bookAuthorName, String bookPrice, String bookPublicationYear, String bookEdition, String bookLocation, String bookCondition, String isbnNumber, String imageLocation1, String imageLocation2, String imageLocation3, String dateAndTime) {
        this.bookId = bookId;
        this.userId = userId;
        this.bookName = bookName;
        this.bookAuthorName = bookAuthorName;
        this.bookPrice = bookPrice;
        this.bookPublicationYear = bookPublicationYear;
        this.bookEdition = bookEdition;
        this.bookLocation = bookLocation;
        this.bookCondition = bookCondition;
        this.isbnNumber = isbnNumber;
        this.imageLocation1 = imageLocation1;
        this.imageLocation2 = imageLocation2;
        this.imageLocation3 = imageLocation3;
        this.dateAndTime = dateAndTime;
    }

    @Ignore
    public BookInfo(long userId, String bookName, String bookAuthorName, String bookPrice, String bookPublicationYear, String bookEdition, String bookLocation, String bookCondition, String isbnNumber, String imageLocation1, String imageLocation2, String imageLocation3, String dateAndTime) {
        this.userId = userId;
        this.bookName = bookName;
        this.bookAuthorName = bookAuthorName;
        this.bookPrice = bookPrice;
        this.bookPublicationYear = bookPublicationYear;
        this.bookEdition = bookEdition;
        this.bookLocation = bookLocation;
        this.bookCondition = bookCondition;
        this.isbnNumber = isbnNumber;
        this.imageLocation1 = imageLocation1;
        this.imageLocation2 = imageLocation2;
        this.imageLocation3 = imageLocation3;
        this.dateAndTime = dateAndTime;
    }

    @Ignore
    public BookInfo(String bookName, String bookAuthorName, String bookPrice, String bookPublicationYear, String bookEdition, String bookLocation, String bookCondition, String isbnNumber, String imageLocation1, String imageLocation2, String imageLocation3, String dateAndTime) {
        this.bookName = bookName;
        this.bookAuthorName = bookAuthorName;
        this.bookPrice = bookPrice;
        this.bookPublicationYear = bookPublicationYear;
        this.bookEdition = bookEdition;
        this.bookLocation = bookLocation;
        this.bookCondition = bookCondition;
        this.isbnNumber = isbnNumber;
        this.imageLocation1 = imageLocation1;
        this.imageLocation2 = imageLocation2;
        this.imageLocation3 = imageLocation3;
        this.dateAndTime = dateAndTime;
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

    public String getIsbnNumber() {
        return isbnNumber;
    }

    public void setIsbnNumber(String isbnNumber) {
        this.isbnNumber = isbnNumber;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthorName() {
        return bookAuthorName;
    }

    public void setBookAuthorName(String bookAuthorName) {
        this.bookAuthorName = bookAuthorName;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookPublicationYear() {
        return bookPublicationYear;
    }

    public void setBookPublicationYear(String bookPublicationYear) {
        this.bookPublicationYear = bookPublicationYear;
    }

    public String getBookEdition() {
        return bookEdition;
    }

    public void setBookEdition(String bookEdition) {
        this.bookEdition = bookEdition;
    }

    public String getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(String bookLocation) {
        this.bookLocation = bookLocation;
    }

    public String getBookCondition() {
        return bookCondition;
    }

    public void setBookCondition(String bookCondition) {
        this.bookCondition = bookCondition;
    }

    public String getImageLocation1() {
        return imageLocation1;
    }

    public void setImageLocation1(String imageLocation1) {
        this.imageLocation1 = imageLocation1;
    }

    public String getImageLocation2() {
        return imageLocation2;
    }

    public void setImageLocation2(String imageLocation2) {
        this.imageLocation2 = imageLocation2;
    }

    public String getImageLocation3() {
        return imageLocation3;
    }

    public void setImageLocation3(String imageLocation3) {
        this.imageLocation3 = imageLocation3;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
