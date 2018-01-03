package com.felix.simplebook.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.felix.simplebook.R;
import com.felix.simplebook.activity.StartActivity;
import com.felix.simplebook.service.WidgetService;

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
        MyLog.info("onReceive...", intent.getAction());
        if (intent.getAction().equals(CLICK_ACTION)) {
            MyLog.info("Click...");
            context.startActivity(new Intent(context, StartActivity.class));
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i = 0; i < appWidgetIds.length; i++) {
            onWidgetUpdate(context, appWidgetManager, appWidgetIds[i]);
        }
        context.startService(new Intent(context, WidgetService.class));
    }

    //更新桌面小部件
    private void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.home_widget);
        //单击小部件的点击事件
        Intent intent = new Intent();
        intent.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.rl_content_home_widget, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
}

