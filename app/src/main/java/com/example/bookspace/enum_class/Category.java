package com.example.bookspace.enum_class;

public enum Category {
    ACADEMIC("academic"),
    NON_ACADEMIC("non_academic");

    private final String category;

    Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
