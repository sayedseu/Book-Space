package com.example.bookspace.model.model_class.remote;

import com.google.gson.annotations.SerializedName;

public class Interested {
    @SerializedName("bookDetails")
    private BookDetails bookDetails;
    @SerializedName("id")
    private Integer id;
    @SerializedName("owner")
    private User user;

    public BookDetails getBookDetails() {
        return bookDetails;
    }

    public void setBookDetails(BookDetails bookDetails) {
        this.bookDetails = bookDetails;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
