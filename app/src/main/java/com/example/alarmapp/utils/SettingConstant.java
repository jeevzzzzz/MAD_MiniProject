package com.example.alarmapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

final public class SettingConstant {
    final static private String REQUEST_FOR_NEARBY_RANGE = "NEARY_RANGE_NOW_WAKE_ALARM";
    final static private String REQUEST_FOR_DEFAULT_RINGSTONE = "REQUEST_FOR_DEFAULT_RINGSTONE";

    private static SharedPreferences pref = null;

    public static String getDefaultRingtone(Context context) {
        if (pref == null) {
            pref = context.getSharedPreferences(REQUEST_FOR_NEARBY_RANGE, 0);
        }
        return pref.getString(REQUEST_FOR_DEFAULT_RINGSTONE, null);
    }

    public static void updateDefaultRingtone(Context context, String value) {
        if (pref == null) {
            pref = context.getSharedPreferences(REQUEST_FOR_NEARBY_RANGE, 0);
        }
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(REQUEST_FOR_DEFAULT_RINGSTONE, value);
        editor.apply();
    }
}
