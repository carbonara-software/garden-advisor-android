package com.alexinnocenzi.gardenadvisor.util;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "GARDENADVISOR";
    public static final String SEP = " - ";
    public static void logi(String message){
        Log.i(TAG,message);
    }
    public static void logd(String message){
        Log.d(TAG,message);
    }
    public static void logw(String message){
        Log.w(TAG,message);
    }

    public static void loge(String message){
        Log.e(TAG,message);
    }
    public static void loge(String message,Exception exception){
        Log.e(TAG,message,exception);
    }
    public static void loge(Exception exception){
        Log.e(TAG,"Exception:",exception);
    }
}
