package com.felix.simplebook.activity;



import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;

import butterknife.BindView;

public class MyCenterActivity extends BaseActivity {

    @BindView(R.id.toolbar_activity_my_center)
    Toolbar mToolbar;

    @Override
    public int initLayout() {
        return R.layout.activity_my_center;
    }

    @Override
    public void initView() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
