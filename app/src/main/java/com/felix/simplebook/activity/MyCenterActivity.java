package com.felix.simplebook.activity;




import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;

import butterknife.BindView;

public class MyCenterActivity extends BaseActivity {

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

    public static final int RESULT_CODE = 200;

    @Override
    public int initLayout() {
        return R.layout.activity_my_center;
    }

    @Override
    public void initView() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvBackUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CODE, getIntent());
                finish();
            }
        });

        tvUnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyCenterActivity.this,
                                LockActivity.class));
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
