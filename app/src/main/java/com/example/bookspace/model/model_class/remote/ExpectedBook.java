package com.example.bookspace.model.model_class.remote;

import com.google.gson.annotations.SerializedName;

public class ExpectedBook {
    @SerializedName("bookAuthorName")
    private String bookAuthorName;
    @SerializedName("bookPrice")
    private String bookPrice;
    @SerializedName("boonName")
    private String boonName;
    @SerializedName("expectedBookId")
    private long expectedBookId;
    @SerializedName("imageLocation1")
    private String imageLocation1;
    @SerializedName("imageLocation2")
    private String imageLocation2;
    @SerializedName("imageLocation3")
    private String imageLocation3;

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

    public String getBoonName() {
        return boonName;
    }

    public void setBoonName(String boonName) {
        this.boonName = boonName;
    }

    public long getExpectedBookId() {
        return expectedBookId;
    }

    public void setExpectedBookId(long expectedBookId) {
        this.expectedBookId = expectedBookId;
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
}
