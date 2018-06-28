package com.felix.simplebook.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
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
import com.felix.simplebook.utils.MyLog;
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
    private SharedPreferences preferences;

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
                    startActivity(new Intent(MyCenterActivity.this,
                            BackupNetActivity.class));
                } else {
                    MyToast.makeText(mContext, mContext.getResources()
                                    .getString(R.string.center_go2_login_show),
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MyCenterActivity.this,
                            LoginActivity.class));
                }
            }
        });

        mOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter.isLogin()) {
                    if (presenter.exitLogin()) {
                        MyToast.makeText(mContext, mContext.getResources()
                                .getString(R.string.center_out_login_show),
                                Toast.LENGTH_SHORT).show();
                        tvUserName.setText("未登录");
                        tvEmail.setText("登录后才能使用网络备份");
                    } else {
                        MyToast.makeText(mContext, mContext.getResources()
                                        .getString(R.string.center_out_fail_show),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    MyToast.makeText(mContext, mContext.getResources()
                                    .getString(R.string.center_no_login_show),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presenter.isLogin()) {
                    presenter.selectImg(MyCenterActivity.this);
                }else{
                    MyToast.makeText(mContext, mContext.getResources()
                                    .getString(R.string.center_go_login_show),
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MyCenterActivity.this,
                            LoginActivity.class));
                }
            }
        });
    }

    @Override
    public void initData() {
        presenter = new MyCenterPresenter(this, MyCenterActivity.this);
        preferences = mContext.getSharedPreferences("config.sb",
                    Context.MODE_PRIVATE);
        tvUserName.setText(preferences.getString("username","未登录"));
        tvEmail.setText(preferences.getString("email","登录后才能使用网络备份"));
        if(presenter.isLogin()){
            String photos = preferences.getString("photos", "no");
            if(!"no".equals(photos)){
                MyLog.info("photos path",photos);
                presenter.downloadImg(mPhotos, photos);
            }
        }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.activityResult(requestCode, resultCode, data, mPhotos);
    }
}
