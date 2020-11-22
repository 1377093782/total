package com.anguomob.total.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.anguomob.total.init.AnguoUtils;


public class SpUtils {

    private static SharedPreferences sp;

    private static String configName_ = "config";

    /**
     * 设置sp 存储的名字
     *
     * @param configName
     */
    public static void setSpConfigName(String configName) {
        configName_ = configName;
    }


    public static void getSharedPreference(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(configName_, context.MODE_PRIVATE);
        }
    }

    public static void putString(Context context, String key, String value) {
        getSharedPreference(context);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        getSharedPreference(context);
        return sp.getString(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        getSharedPreference(context);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        getSharedPreference(context);
        return sp.getInt(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getSharedPreference(context);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        getSharedPreference(context);
        return sp.getBoolean(key, defValue);
    }

    public static void putLong(Context context, String key, Long value) {
        getSharedPreference(context);
        sp.edit().putLong(key, value).commit();
    }

    public static Long getLong(Context context, String key,
                               Long defValue) {
        getSharedPreference(context);
        return sp.getLong(key, defValue);
    }


    /**
     * 移除
     */
    public static void remove(Context context, String key) {
        getSharedPreference(context);
        sp.edit().remove(key).commit();
    }

    //单参数构造
    public static void putString(String key, String value) {
        getSharedPreference(AnguoUtils.getContext());
        sp.edit().putString(key, value).commit();
    }

    public static String getString(String key, String defValue) {
        getSharedPreference(AnguoUtils.getContext());
        return sp.getString(key, defValue);
    }

    public static void putInt(String key, int value) {
        getSharedPreference(AnguoUtils.getContext());
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, int defValue) {
        getSharedPreference(AnguoUtils.getContext());
        return sp.getInt(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        getSharedPreference(AnguoUtils.getContext());
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key,
                                     boolean defValue) {
        getSharedPreference(AnguoUtils.getContext());
        return sp.getBoolean(key, defValue);
    }

    public static void putLong(String key, Long value) {
        getSharedPreference(AnguoUtils.getContext());
        sp.edit().putLong(key, value).commit();
    }

    public static Long getLong(String key,
                               Long defValue) {
        getSharedPreference(AnguoUtils.getContext());
        return sp.getLong(key, defValue);
    }


    /**
     * 移除
     */
    public static void remove(String key) {
        getSharedPreference(AnguoUtils.getContext());
        sp.edit().remove(key).commit();
    }


}  
