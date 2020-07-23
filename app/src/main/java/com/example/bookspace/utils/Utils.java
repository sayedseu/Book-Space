package com.example.bookspace.utils;

import com.example.bookspace.R;
import com.example.bookspace.model.model_class.local.ViewCategory;

import java.util.ArrayList;
import java.util.List;

public final class Utils {

    public static final int SIGNUP_FRAGMENT = 1;
    public static final int SIGNIN_FRAGMENT = 0;
    public static final String OTP_KEY = "otp";
    public static final String RESET_PASSWORD_KEY = "reset";

    public static List<ViewCategory> getViewCategoryList() {
        List<ViewCategory> viewCategoryList = new ArrayList<>();
        viewCategoryList.add(new ViewCategory("School", R.drawable.school));
        viewCategoryList.add(new ViewCategory("Collage", R.drawable.collage));
        viewCategoryList.add(new ViewCategory("University", R.drawable.university));
        viewCategoryList.add(new ViewCategory("Medical", R.drawable.medical));
        viewCategoryList.add(new ViewCategory("Engineering", R.drawable.engineering));
        viewCategoryList.add(new ViewCategory("Adventure", R.drawable.adventure));
        viewCategoryList.add(new ViewCategory("Detective", R.drawable.detective));
        viewCategoryList.add(new ViewCategory("Science Fiction", R.drawable.science));
        viewCategoryList.add(new ViewCategory("Novel", R.drawable.novel));
        viewCategoryList.add(new ViewCategory("Comic", R.drawable.comic));
        viewCategoryList.add(new ViewCategory("Biography", R.drawable.biography));
        viewCategoryList.add(new ViewCategory("Essay", R.drawable.eassy));
        viewCategoryList.add(new ViewCategory("Periodicals", R.drawable.periodicals));
        viewCategoryList.add(new ViewCategory("Development", R.drawable.development));
        return viewCategoryList;
    }
}
