package com.felix.simplebook.activity;

import android.widget.TextView;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.utils.ApkVersionUtils;

import butterknife.BindView;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_version_name_activity_about)
    TextView tvVersionName;

    @Override
    public int initLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        setFinishOnTouchOutside(true);
    }

    @Override
    public void initData() {
        tvVersionName.setText(ApkVersionUtils.getVersionName(mContext));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
