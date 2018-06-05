package com.felix.simplebook.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.felix.simplebook.utils.AutoBackUp;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    public Unbinder bind;
    private static List<Activity> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        bind = ButterKnife.bind(this);
        mContext = getApplicationContext();
        list.add(this);
        initView();
        initData();
        //强制横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //获取布局
    public abstract int initLayout();

    //初始化控件
    public abstract void initView();

    //初始化数据
    public abstract void initData();

    //防止字体大小随系统改变
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    long currentTime;
    boolean flag = true;

    @Override
    public void onBackPressed() {
        if (flag) {
            currentTime = System.currentTimeMillis();
            MyToast.makeText(BaseActivity.this, "亲、再按一次退出简账", Toast.LENGTH_SHORT).show();
        }
        MyLog.info("currentTime-" + currentTime);
        if (System.currentTimeMillis() - currentTime < 2000
                && System.currentTimeMillis() - currentTime > 100) {
            //自动备份数据
            new AutoBackUp().startBackup();

            for (Activity activity : list){
                activity.finish();
            }
        }
        if(flag){
            flag = false;
        }else{
            flag = true;
        }
    }

    public void finishAll(){
        for (Activity activity : list){
            activity.finish();
        }
    }
}
