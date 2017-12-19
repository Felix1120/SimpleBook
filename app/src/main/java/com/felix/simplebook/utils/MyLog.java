package com.felix.simplebook.utils;

import android.util.Log;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class MyLog {
    public static void info(String... values) {
        String title = values[0];
        if (values.length > 1) {
            String body = values[1];
            Log.i("Felix", title + ": " + body);
            return;
        }else {
            Log.i("Felix", title);
            return;
        }
    }
}
