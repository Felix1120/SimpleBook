package com.felix.simplebook.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.felix.simplebook.R;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class MyToast {
    private static Toast mToast;
    private static Context mContext;

    private MyToast(Activity activity, String msg, int time) {
        mContext = activity.getApplicationContext();
        mToast = new Toast(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_toast_item, null);
        TextView tvShow = view.findViewById(R.id.tv_msg_my_toast_item);
        tvShow.setText(msg);
        mToast.setDuration(time);
        mToast.setView(view);
    }

    public static MyToast makeText(Activity activity, String msg, int time) {
        return new MyToast(activity, msg, time);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }
}
