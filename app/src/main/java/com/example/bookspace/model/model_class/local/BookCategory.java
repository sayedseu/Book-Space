package com.example.bookspace.model.model_class.local;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class BookCategory {
    @PrimaryKey(autoGenerate = true)
    private long categoryId;
    private long bookInfoId;
    private String mainCategory;
    private String subCategory;

    public BookCategory(long categoryId, long bookInfoId, String mainCategory, String subCategory) {
        this.categoryId = categoryId;
        this.bookInfoId = bookInfoId;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
    }

    @Ignore
    public BookCategory(long bookInfoId, String mainCategory, String subCategory) {
        this.bookInfoId = bookInfoId;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
    }

    @Ignore
    public BookCategory(String mainCategory, String subCategory) {
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getBookInfoId() {
        return bookInfoId;
    }

    public void setBookInfoId(long bookInfoId) {
        this.bookInfoId = bookInfoId;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
}
