package com.example.bookspace.enum_class;

public enum Education {
    SCHOOL("School"),
    COLLAGE("Collage"),
    UNIVERSITY("University"),
    NONE("None");

    private final String education;

    Education(String education) {
        this.education = education;
    }

    public String getEducation() {
        return education;
    }
}
