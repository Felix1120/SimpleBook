package com.felix.simplebook.utils;

import android.content.Context;

/**
 * Created by chaofei.xue on 2018/7/5.
 */

public class ApkVersionUtils {

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context
                    .getPackageName(), 0).versionName;
        } catch (Exception e) {
            MyLog.info("getVersionName", e.toString());
        }
        return versionName;
    }
}
