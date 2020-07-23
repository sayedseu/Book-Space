package com.example.bookspace.enum_class;

public enum Mode {
    SELL("Sell"),
    DONATE("Donate"),
    RENT("Rent"),
    EXCHANGE("Exchange"),
    EXCHANGE_WITH_CASH("Exchange With Cash");

    private final String mode;

    Mode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
