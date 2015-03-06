package org.fedoraproject.mobile.utils;

import android.util.Log;

import org.fedoraproject.mobile.BuildConfig;

/**
 * Easy manage log in debug/release
 *
 * Created by Julien De Nadai on 01/11/2014.
 */
public class LogUtil {
    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG, getThread() + msg);
        }
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(Constants.TAG, getThread() + msg);
        }
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(Constants.TAG, getThread() + msg);;
            Thread.dumpStack();
        }
    }

    public static void wtf(String msg) {
        if (BuildConfig.DEBUG) {
            Log.wtf(Constants.TAG, getThread() + msg);
            Thread.dumpStack();
        }
    }

    public static void v(String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(Constants.TAG, getThread() + msg);
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(Constants.TAG, getThread() + msg);
            Thread.dumpStack();
        }
    }

    public static String getThread() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
//        return stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber();
        return Thread.currentThread().getName() + "-" + stackTraceElement.toString();
    }
}
