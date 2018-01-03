package com.felix.simplebook.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.felix.simplebook.R;
import com.felix.simplebook.activity.HomeActivity;
import com.felix.simplebook.utils.MyAppWidgetProvider;
import com.felix.simplebook.utils.MyLog;

/**
 * Created by chaofei.xue on 2018/1/2.
 */
public class WidgetService extends Service {
    private static final int ALARM_DURATION  = 60 * 60 * 1000; // service 自启间隔
    private static final int UPDATE_DURATION = 3 * 1000;     // Widget 更新间隔
    private static final int UPDATE_MESSAGE  = 1000;
    private static Intent intent;

    private UpdateHandler updateHandler; // 更新 Widget 的 Handler

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 每个 ALARM_DURATION 自启一次
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getBaseContext(), WidgetService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getBaseContext(), 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + ALARM_DURATION, pendingIntent);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        updateHandler = new UpdateHandler();
        MyLog.info("service被启动");
        Message message = updateHandler.obtainMessage();
        message.what = UPDATE_MESSAGE;
        updateHandler.sendMessageDelayed(message, UPDATE_DURATION);

        //注册广播接收器
        UpdateReceiver update = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(HomeActivity.UPDATE_ACTION);
        registerReceiver(update, filter);
    }

    private void updateWidget() {
        // 更新 Widget
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("info");
            String month = bundle.getString("month");
            String day = bundle.getString("day");
            String monthIn = bundle.getString("monthIn");
            String monthOut = bundle.getString("monthOut");
            String dayIn = bundle.getString("dayIn");
            String dayOut = bundle.getString("dayOut");

            MyLog.info("intent 不为空 moth = " + month);

            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(),
                    R.layout.home_widget);
            remoteViews.setTextViewText(R.id.tv_month_home_widget, month + "月");
            remoteViews.setTextViewText(R.id.tv_day_home_widget, day + "日");
            remoteViews.setTextViewText(R.id.tv_month_in_home_widget, monthIn);
            remoteViews.setTextViewText(R.id.tv_month_out_home_widget, monthOut);
            remoteViews.setTextViewText(R.id.tv_day_in_home_widget, dayIn);
            remoteViews.setTextViewText(R.id.tv_day_out_home_widget, dayOut);
            MyLog.info("MyAppWidgetProvider", month + " " + day + "");

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            appWidgetManager.updateAppWidget(new ComponentName(this, MyAppWidgetProvider.class),
                    remoteViews);
        }

        // 发送下次更新的消息
        Message message = updateHandler.obtainMessage();
        message.what = UPDATE_MESSAGE;
        updateHandler.sendMessageDelayed(message, UPDATE_DURATION);
    }

    protected final class UpdateHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_MESSAGE:
                    updateWidget();
                    break;
                default:
                    break;
            }
        }
    }

    private class UpdateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intents) {
            intent = intents;
            MyLog.info("UpdateReceiver 收到信息");
        }
    }
}