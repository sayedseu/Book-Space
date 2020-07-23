package com.example.bookspace.enum_class;

public enum IsStudent {
    YES("Yes"),
    NO("NO");

    private final String isStudent;

    IsStudent(String isStudent) {
        this.isStudent = isStudent;
    }

    public String getIsStudent() {
        return isStudent;
    }
}
