package com.felix.simplebook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.presenter.BackupNetPresenter;
import com.felix.simplebook.presenter.IBackupNetPresenter;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.MyToast;
import com.felix.simplebook.view.IBackupNetView;

import java.lang.ref.WeakReference;

import butterknife.BindView;

public class BackupNetActivity extends BaseActivity implements IBackupNetView {

    @BindView(R.id.btn_back_activity_backup_net)
    Button btnBack;

    @BindView(R.id.btn_download_activity_backup_net)
    Button btnRestore;

    @BindView(R.id.btn_up_activity_backup_net)
    Button btnBackup;

    @BindView(R.id.tv_time_up_activity_backup_net)
    TextView tvTimeBackup;

    @BindView(R.id.tv_time_download_activity_backup_net)
    TextView tvTimeRestore;

    @BindView(R.id.img_loading_activity_backup_net)
    ImageView imgLoading;

    private IBackupNetPresenter presenter;

    private MyHandler myHandler = new MyHandler(this);

    private class MyHandler extends Handler {

        private WeakReference<Activity> weakReference;

        public MyHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                MyToast.makeText(weakReference.get(), msg.obj.toString(), Toast.LENGTH_SHORT)
                        .show();
                closeLoading();
            }else if(msg.what == 9){
                setMyTime(msg.getData().getString("bTime"), msg.getData().getString("rTime"));
            }
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_backup_net;
    }

    @Override
    public void initView() {

        presenter = new BackupNetPresenter(this, mContext);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.info("btnBackup","onclick");
                presenter.backup();
            }
        });

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.restore();
            }
        });
    }

    @Override
    public void initData() {
        presenter.getTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        myHandler.removeCallbacks(null);
    }

    @Override
    public void setTime(String bTime, String rTime) {
        Message message = Message.obtain();
        message.what = 9;
        Bundle bundle = new Bundle();
        bundle.putString("bTime", bTime);
        bundle.putString("rTime", rTime);
        message.setData(bundle);
        myHandler.sendMessage(message);
    }

    @Override
    public void showMessage(String value) {
        Message message = Message.obtain();
        message.obj = value;
        message.what = 1;
        myHandler.sendMessage(message);
    }

    @Override
    public void showLoading() {
        btnBackup.setEnabled(false);
        btnBack.setEnabled(false);
        btnRestore.setEnabled(false);
        imgLoading.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.loading);
        imgLoading.startAnimation(animation);
    }

    @Override
    public void closeLoading() {
        btnBackup.setEnabled(true);
        btnBack.setEnabled(true);
        btnRestore.setEnabled(true);
        imgLoading.clearAnimation();
        imgLoading.setVisibility(View.GONE);
    }

    private void setMyTime(String bTime, String rTime) {
        tvTimeBackup.setText(bTime);
        tvTimeRestore.setText(rTime);
    }
}
