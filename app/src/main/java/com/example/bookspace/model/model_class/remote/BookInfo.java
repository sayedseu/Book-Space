package com.example.bookspace.model.model_class.remote;

import com.google.gson.annotations.SerializedName;

public class BookInfo {
    @SerializedName("bookAuthorName")
    private String bookAuthorName;
    @SerializedName("bookCondition")
    private String bookCondition;
    @SerializedName("bookEdition")
    private String bookEdition;
    @SerializedName("bookId")
    private long bookId;
    @SerializedName("bookLocation")
    private String bookLocation;
    @SerializedName("bookName")
    private String bookName;
    @SerializedName("bookPrice")
    private String bookPrice;
    @SerializedName("bookPublicationYear")
    private String bookPublicationYear;
    @SerializedName("dateAndTime")
    private String dateAndTime;
    @SerializedName("imageLocation1")
    private String imageLocation1;
    @SerializedName("imageLocation2")
    private String imageLocation2;
    @SerializedName("imageLocation3")
    private String imageLocation3;
    @SerializedName("isbnNumber")
    private String isbnNumber;

    public String getBookAuthorName() {
        return bookAuthorName;
    }

    public void setBookAuthorName(String bookAuthorName) {
        this.bookAuthorName = bookAuthorName;
    }

    public String getBookCondition() {
        return bookCondition;
    }

    public void setBookCondition(String bookCondition) {
        this.bookCondition = bookCondition;
    }

    public String getBookEdition() {
        return bookEdition;
    }

    public void setBookEdition(String bookEdition) {
        this.bookEdition = bookEdition;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(String bookLocation) {
        this.bookLocation = bookLocation;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
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

    public String getIsbnNumber() {
        return isbnNumber;
    }

    public void setIsbnNumber(String isbnNumber) {
        this.isbnNumber = isbnNumber;
    }

}
