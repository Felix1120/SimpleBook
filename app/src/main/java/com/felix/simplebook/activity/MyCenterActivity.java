package com.felix.simplebook.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.presenter.IMyCenterPresenter;
import com.felix.simplebook.presenter.MyCenterPresenter;
import com.felix.simplebook.utils.MyToast;
import com.felix.simplebook.view.IMyCenterView;

import butterknife.BindView;

public class MyCenterActivity extends BaseActivity implements IMyCenterView {

    @BindView(R.id.toolbar_activity_my_center)
    Toolbar mToolbar;
    @BindView(R.id.img_photos_activity_my_center)
    ImageView mPhotos;
    @BindView(R.id.img_edit_activity_my_center)
    ImageView mEdit;
    @BindView(R.id.img_other_activity_my_center)
    ImageView mOther;
    @BindView(R.id.img_out_activity_my_center)
    ImageView mOut;
    @BindView(R.id.tv_backup_activity_my_center)
    TextView tvBackUp;
    @BindView(R.id.tv_backup_net_activity_my_center)
    TextView tvBackUpNet;
    @BindView(R.id.tv_unlock_activity_my_center)
    TextView tvUnLock;
    @BindView(R.id.tv_good_activity_my_center)
    TextView tvGood;
    @BindView(R.id.tv_username_activity_my_center)
    TextView tvUserName;
    @BindView(R.id.tv_email_activity_my_center)
    TextView tvEmail;

    private IMyCenterPresenter presenter;

    @Override
    public int initLayout() {
        return R.layout.activity_my_center;
    }

    @Override
    public void initView() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvBackUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyCenterActivity.this,
                        HomeActivity.class).setAction("center"));
                finishAll();
            }
        });

        tvUnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyCenterActivity.this,
                        LockActivity.class));
            }
        });

        tvBackUpNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter.isLogin()) {

                } else {
                    MyToast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MyCenterActivity.this,
                            LoginActivity.class));
                }
            }
        });

        mOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presenter.isLogin()){
                    if(presenter.exitLogin()){
                        MyToast.makeText(mContext, "已退出登录", Toast.LENGTH_SHORT).show();
                        tvUserName.setText("未登录");
                        tvEmail.setText("登录后才能使用网络备份");
                    }else {
                        MyToast.makeText(mContext, "退出失败", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    MyToast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void initData() {
        presenter = new MyCenterPresenter(this, mContext);
        SharedPreferences preferences = mContext.getSharedPreferences("config.sb",
                    Context.MODE_PRIVATE);
        tvUserName.setText(preferences.getString("username","未登录"));
        tvEmail.setText(preferences.getString("email","登录后才能使用网络备份"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public static void startMyActivity(Context context, String who) {
        Intent intent = new Intent(context, MyCenterActivity.class);
        intent.putExtra("who", who);
        context.startActivity(intent);
    }
}
