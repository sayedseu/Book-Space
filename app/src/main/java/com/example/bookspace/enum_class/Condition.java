package com.example.bookspace.enum_class;

public enum Condition {
    NEW("New"),
    USED("Used"),
    FAIR("Fair");

    private final String condition;

    Condition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
