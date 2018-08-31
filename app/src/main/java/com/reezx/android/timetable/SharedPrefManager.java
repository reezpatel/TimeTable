package com.reezx.android.timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import static com.reezx.android.timetable.Constants.DEBUG;
import static com.reezx.android.timetable.Constants.SHARED_PREF_FILENAME;
import static com.reezx.android.timetable.Constants.SIGNINED;
import static com.reezx.android.timetable.Constants.TEXT_EMAIL;
import static com.reezx.android.timetable.Constants.TEXT_ID;
import static com.reezx.android.timetable.Constants.TEXT_LOGIN;
import static com.reezx.android.timetable.Constants.TEXT_NAME;
import static com.reezx.android.timetable.Constants.TEXT_PHOTO_URL;

/**
 * Created by reezpatel on 01-Jul-17.
 */

public class SharedPrefManager {
    public static boolean isLoggedIn(Context context) {
        if (DEBUG) {
            return SIGNINED;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILENAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(TEXT_LOGIN,false);
    }

    public static void removeLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILENAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TEXT_LOGIN,false);
        editor.apply();
    }

    public static void setLogin(Context context, String id, String name, String email, Uri photoURL) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILENAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TEXT_LOGIN,true);
        editor.putString(TEXT_ID,id);
        editor.putString(TEXT_NAME,name);
        editor.putString(TEXT_EMAIL,email);
        editor.putString(TEXT_PHOTO_URL,photoURL.toString());
        editor.apply();
    }

    public static Bundle getUserData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILENAME,Context.MODE_PRIVATE);
        Bundle bundle = new Bundle();
        bundle.putString(TEXT_ID,sharedPreferences.getString(TEXT_ID,"0"));
        bundle.putString(TEXT_NAME,sharedPreferences.getString(TEXT_NAME,"0"));
        bundle.putString(TEXT_EMAIL,sharedPreferences.getString(TEXT_EMAIL,"0"));
        bundle.putString(TEXT_PHOTO_URL,sharedPreferences.getString(TEXT_PHOTO_URL,"0"));
        return bundle;
    }


}
