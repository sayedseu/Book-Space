package com.example.bookspace.model.model_class.remote;

import com.google.gson.annotations.SerializedName;

public class BookCategory {
    @SerializedName("categoryId")
    private long categoryId;
    @SerializedName("mainCategory")
    private String mainCategory;
    @SerializedName("subCategory")
    private String subCategory;

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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
