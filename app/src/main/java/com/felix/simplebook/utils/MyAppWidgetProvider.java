package com.felix.simplebook.utils;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.felix.simplebook.activity.StartActivity;

/**
 * Created by chaofei.xue on 2017/12/29.
 */

public class MyAppWidgetProvider extends AppWidgetProvider {
    public static final String CLICK_ACTION = "com.felix.simplebook.click";

    public MyAppWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        MyLog.info("onReceive...");
        if (intent.getAction().equals(CLICK_ACTION)) {
            MyLog.info("Click...");
            context.startActivity(new Intent(context, StartActivity.class));
        }
    }
}

