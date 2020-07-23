package com.example.bookspace.model.model_class.local;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ExpectedBook {
    @PrimaryKey(autoGenerate = true)
    private long expectedBookId;
    private long bookIfoId;
    private String boonName;
    private String bookAuthorName;
    private String bookPrice;
    private String imageLocation1;
    private String imageLocation2;
    private String imageLocation3;

    public ExpectedBook(long expectedBookId, long bookIfoId, String boonName, String bookAuthorName, String bookPrice, String imageLocation1, String imageLocation2, String imageLocation3) {
        this.expectedBookId = expectedBookId;
        this.bookIfoId = bookIfoId;
        this.boonName = boonName;
        this.bookAuthorName = bookAuthorName;
        this.bookPrice = bookPrice;
        this.imageLocation1 = imageLocation1;
        this.imageLocation2 = imageLocation2;
        this.imageLocation3 = imageLocation3;
    }

    @Ignore
    public ExpectedBook(long bookIfoId, String boonName, String bookAuthorName, String bookPrice, String imageLocation1, String imageLocation2, String imageLocation3) {
        this.bookIfoId = bookIfoId;
        this.boonName = boonName;
        this.bookAuthorName = bookAuthorName;
        this.bookPrice = bookPrice;
        this.imageLocation1 = imageLocation1;
        this.imageLocation2 = imageLocation2;
        this.imageLocation3 = imageLocation3;
    }

    @Ignore
    public ExpectedBook(String boonName, String bookAuthorName, String bookPrice, String imageLocation1, String imageLocation2, String imageLocation3) {
        this.boonName = boonName;
        this.bookAuthorName = bookAuthorName;
        this.bookPrice = bookPrice;
        this.imageLocation1 = imageLocation1;
        this.imageLocation2 = imageLocation2;
        this.imageLocation3 = imageLocation3;
    }

    public long getExpectedBookId() {
        return expectedBookId;
    }

    public void setExpectedBookId(long expectedBookId) {
        this.expectedBookId = expectedBookId;
    }

    public long getBookIfoId() {
        return bookIfoId;
    }

    public void setBookIfoId(long bookIfoId) {
        this.bookIfoId = bookIfoId;
    }

    public String getBoonName() {
        return boonName;
    }

    public void setBoonName(String boonName) {
        this.boonName = boonName;
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
