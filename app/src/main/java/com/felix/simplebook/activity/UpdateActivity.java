package com.felix.simplebook.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.presenter.UpdatePresenter;
import com.felix.simplebook.utils.ApkVersionUtils;
import com.felix.simplebook.view.IUpdateView;

import butterknife.BindView;

public class UpdateActivity extends BaseActivity implements IUpdateView {
    @BindView(R.id.tv_body_activity_update)
    TextView tvBody;
    @BindView(R.id.tv_local_version_activity_update)
    TextView tvLocalVersion;
    @BindView(R.id.tv_server_version_activity_update)
    TextView tvServerVersion;
    @BindView(R.id.tv_cancel_activity_update)
    TextView tvCancel;
    @BindView(R.id.tv_download_activity_update)
    TextView tvDownload;
    @BindView(R.id.tv_cool_download_activity_update)
    TextView tvCoolDownload;

    private SharedPreferences preferences;
    private UpdatePresenter presenter;

    @Override
    public int initLayout() {
        return R.layout.activity_update;
    }

    @Override
    public void initView() {
        tvLocalVersion.setText(getResources().getString(
                R.string.local_activity_update) + ApkVersionUtils.getVersionName(mContext));

        MyOnClickListener onClickListener = new MyOnClickListener();

        tvCancel.setOnClickListener(onClickListener);
        tvDownload.setOnClickListener(onClickListener);
        tvCoolDownload.setOnClickListener(onClickListener);

        presenter = new UpdatePresenter(this, mContext);

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String serverVersion = "";
        if (intent != null) {
            serverVersion = intent.getStringExtra("serverVersion");
            String messageBody = intent.getStringExtra("messageBody");
            tvBody.setText(messageBody);
            tvServerVersion.setText(getResources().getString(
                    R.string.server_activity_update) + serverVersion);
        } else {
            finish();
        }

        if (ApkVersionUtils.getVersionName(mContext).equals(serverVersion)) {
            tvDownload.setVisibility(View.GONE);
            tvCoolDownload.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }


    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_cancel_activity_update:
                    preferences = getSharedPreferences("config.sb",
                            Context.MODE_PRIVATE);
                    preferences.edit().putString("update_show", "no").apply();
                    finish();
                    break;
                case R.id.tv_download_activity_update:
                    presenter.localDownload("http://47.106.219.34:8080/jz_server/Restore?username=SimpleBook.apk");
                    break;
                case R.id.tv_cool_download_activity_update:
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.coolapk.com/apk/com.felix.simplebook"));
                    startActivity(intent);
                    break;
            }
        }
    }
}
