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
    public static final String UPDATE_ACTION = "com.felix.simplebook.update.widget";

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
        } else if (intent.getAction().equals(UPDATE_ACTION)) {
            MyLog.info("update...");
            Bundle bundle = intent.getBundleExtra("info");
            String month = bundle.getString("month");
            String day = bundle.getString("day");
            String monthIn = bundle.getString("monthIn");
            String monthOut = bundle.getString("monthOut");
            String dayIn = bundle.getString("dayIn");
            String dayOut = bundle.getString("dayOut");
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manager = AppWidgetManager.getInstance
                    (context.getApplicationContext());
            //获得所有本程序创建的appwidget
            ComponentName componentName = new ComponentName(context.getApplicationContext(),
                    MyAppWidgetProvider.class);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.home_widget);
            remoteViews.setTextViewText(R.id.tv_month_home_widget, month + "月");
            remoteViews.setTextViewText(R.id.tv_day_home_widget, day + "日");
            remoteViews.setTextViewText(R.id.tv_month_in_home_widget, monthIn);
            remoteViews.setTextViewText(R.id.tv_month_out_home_widget, monthOut);
            remoteViews.setTextViewText(R.id.tv_day_in_home_widget, dayIn);
            remoteViews.setTextViewText(R.id.tv_day_out_home_widget, dayOut);
            MyLog.info("MyAppWidgetProvider", month + " " + day + "");
            manager.updateAppWidget(componentName, remoteViews);
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

