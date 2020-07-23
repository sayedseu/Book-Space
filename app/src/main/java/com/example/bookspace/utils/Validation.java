package com.example.bookspace.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validation {

    public static boolean isNumberValid(String number) {
        return (number.trim().startsWith("017")
                || number.trim().startsWith("018")
                || number.trim().startsWith("016")
                || number.trim().startsWith("015")
                || number.trim().startsWith("019")
                || number.trim().startsWith("013"))
                && number.trim().length() == 11;
    }

    public static boolean isValidEmail(String email) {
        final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
