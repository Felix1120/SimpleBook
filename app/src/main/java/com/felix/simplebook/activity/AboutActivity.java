package com.felix.simplebook.activity;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class AboutActivity extends BaseActivity {

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

    }
}
