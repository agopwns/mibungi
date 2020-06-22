/*
 * Copyright (C) 2015 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.example.mibungi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public final class PreferencesUtility {

    // 회원 정보
    public static final String LOGIN_ID = "LOGIN_ID";
    public static final String IS_LOGIN = "IS_LOGIN";
    private final String TAG = "PreferencesUtility";

    private static PreferencesUtility sInstance;

    private static SharedPreferences mPreferences;
    private static Context context;
    private ConnectivityManager connManager = null;

    public PreferencesUtility(final Context context) {
        this.context = context;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static final PreferencesUtility getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesUtility(context.getApplicationContext());
        }
        return sInstance;
    }


    public void setOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

//--------------------------------------------------------------------------------------------------



    public boolean isLoginStatus() {
        return mPreferences.getBoolean(IS_LOGIN, false);
    }

    public void setLoginStatus(final boolean b) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(IS_LOGIN, b);
        editor.apply();
    }

    public void setString(String key, String value){
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key){
        return mPreferences.getString(key, "");
    }


    public void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences.Editor editor = mPreferences.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    public ArrayList<String> getStringArrayPref(Context context, String key) {
        String json = mPreferences.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }


}

