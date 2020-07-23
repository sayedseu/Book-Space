package com.example.bookspace.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bookspace.model.model_class.remote.User;

public class SessionManager {
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Session";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_STUDENT = "is_student";
    private static final String KEY_EDUCATION_LEVEL = "education_level";
    private static SessionManager sessionManager;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public static SessionManager getInstance(Context context) {
        if (sessionManager == null) {
            synchronized (SessionManager.class) {
                if (sessionManager == null) {
                    sessionManager = new SessionManager(context);
                }
            }
        }
        return sessionManager;
    }

    public void createLoginSession(User user) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putLong(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhoneNumber());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putBoolean(KEY_IS_STUDENT, user.getStudent());
        editor.putString(KEY_EDUCATION_LEVEL, user.getEducationLevel());
        editor.commit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public User getUserDetails() {
        User user = new User();
        user.setUserId(preferences.getLong(KEY_USER_ID, -1));
        user.setName(preferences.getString(KEY_NAME, null));
        user.setEmail(preferences.getString(KEY_EMAIL, null));
        user.setPhoneNumber(preferences.getString(KEY_PHONE, null));
        user.setPassword(preferences.getString(KEY_PASSWORD, null));
        user.setStudent(preferences.getBoolean(KEY_IS_STUDENT, false));
        user.setEducationLevel(preferences.getString(KEY_EDUCATION_LEVEL, null));
        return user;
    }
}