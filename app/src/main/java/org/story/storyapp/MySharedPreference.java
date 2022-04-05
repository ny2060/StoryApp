package org.story.storyapp;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {

    public static final String PREFERENCE = "user_id";

    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }

    public static void setId(Context context, String id) {
        SharedPreferences prefs = getPreference(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id", id);
        editor.apply();
    }

    public static String getId(Context context) {
        SharedPreferences prefs = getPreference(context);
        String id = prefs.getString("id", "");
        return id;
    }

    public static void setLan(Context context, String lan) {
        SharedPreferences prefs = getPreference(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lan", lan);
        editor.apply();
    }

    public static String getLan(Context context) {
        SharedPreferences prefs = getPreference(context);
        String lan = prefs.getString("lan", "");
        return lan;
    }


    public static void setPageId(Context context, int id) {
        SharedPreferences prefs = getPreference(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("pageId", id);
        editor.apply();
    }

    public static int getPageId(Context context) {
        SharedPreferences prefs = getPreference(context);
        int lan = prefs.getInt("pageId", -1);
        return lan;
    }


    public static void clear(Context context) {
        SharedPreferences prefs = getPreference(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}
