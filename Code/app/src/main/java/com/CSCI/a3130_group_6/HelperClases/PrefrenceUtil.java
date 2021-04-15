package com.CSCI.a3130_group_6.HelperClases;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Helper class for Map functionality
 *
 * @author  Sreyas
 */
public class PrefrenceUtil {

    public static void firstTimeAskingPermission(Context context, String permission, boolean isFirstTime, String prefName){
        SharedPreferences sharedPreference = context.getSharedPreferences(prefName, MODE_PRIVATE);
        sharedPreference.edit().putBoolean(permission, isFirstTime).apply();
    }
    public static boolean isFirstTimeAskingPermission(Context context, String permission, String prefName){
        return context.getSharedPreferences(prefName, MODE_PRIVATE).getBoolean(permission, true);
    }
}

