package com.num.myspeedtest.controller.helpers;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Joseph Lee on 12/12/14.
 */
public class Logger {

    public static void show(String log) {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        for (StackTraceElement s : st) {
            String className = s.getClassName();
            if (className.startsWith("com.num.myspeedtest.")
                && !className.equalsIgnoreCase("com.num.myspeedtest.helpers.Logger")) {
                className = className.split("\\.")[(className.split("\\.")).length - 1];
                Log.d(className, log);
                break;
            }
        }
    }

    public static void show(String tag, String log){
        Log.d(tag, log);
    }
}
