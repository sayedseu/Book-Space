package com.example.bookspace.model.model_class.local;

import androidx.room.Embedded;
import androidx.room.Relation;

public class InterestedWithUser {
    @Embedded
    public Interested interested;
    @Relation(
            parentColumn = "bookId",
            entityColumn = "bookId"
    )
    public BookInfo bookInfo;

    @Relation(
            parentColumn = "modeId",
            entityColumn = "modeId"
    )
    public BookMode bookMode;

    @Relation(
            parentColumn = "categoryId",
            entityColumn = "categoryId"
    )
    public BookCategory bookCategory;

    @Relation(
            parentColumn = "expectedBookId",
            entityColumn = "expectedBookId"
    )
    public ExpectedBook expectedBook;
}
