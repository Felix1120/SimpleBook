package com.felix.simplebook.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.fragment.AddFragment;
import com.felix.simplebook.fragment.BackupFragment;
import com.felix.simplebook.fragment.HomeFragment;
import com.felix.simplebook.fragment.ManagerFragment;
import com.felix.simplebook.presenter.HomePresenter;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.NetInfoType;
import com.felix.simplebook.view.IHomeView;

import butterknife.BindView;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class HomeActivity extends BaseActivity implements IHomeView {

    public static final String UPDATE_ACTION = "com.felix.simplebook.update.widget";

    @BindView(R.id.toolbar_activity_home)
    Toolbar mToolbar;

    @BindView(R.id.dl_activity_home)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.fab_activity_home)
    FloatingActionButton mActionButton;

    @BindView(R.id.nav_activity_home)
    NavigationView mNavigationView;

    private Fragment currentFragment;
    private HomeFragment homeFragment;
    private BackupFragment backupFragment;
    private AddFragment addFragment;
    private ActionBar actionBar;

    private HomePresenter presenter;

    @Override
    public int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        homeFragment = new HomeFragment();
        backupFragment = new BackupFragment();
        addFragment = new AddFragment();

        presenter = new HomePresenter(this, mContext);

        if (getIntent().getAction() != null && getIntent().getAction().equals("center")) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_activity_home, backupFragment)
                    .commit();
            currentFragment = backupFragment;
        } else {
            //设置默认界面
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_activity_home, homeFragment)
                    .commit();
            currentFragment = homeFragment;
        }
        //设置滑动菜单默认选中项
        mNavigationView.setCheckedItem(R.id.my_home);
        //设置监听器
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.
                OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.my_home:
                        mActionButton.setVisibility(View.VISIBLE);
                        switchFragment(homeFragment);
                        break;
                    case R.id.manager:
                        mActionButton.setVisibility(View.GONE);
                        switchFragment(new ManagerFragment());
                        break;
//                    case R.id.lock:
//                        startActivity(new Intent(HomeActivity.this,
//                                LockActivity.class));
//                        break;
//                    case R.id.backup:
//                        mActionButton.setVisibility(View.GONE);
//                        switchFragment(backupFragment);
//                        break;
                    case R.id.about:
                        startActivity(new Intent(HomeActivity.this,
                                AboutActivity.class));
                        break;
                    case R.id.add_type:
                        mActionButton.setVisibility(View.GONE);
                        switchFragment(addFragment);
                        break;
                    case R.id.my_center:
                        startActivity(new Intent(HomeActivity.this,
                                MyCenterActivity.class));
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void initData() {
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, HomeShowActivity.class));
            }
        });

        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                    ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        presenter.query();

        //test
        //startUpdateActivity("1.5.7", "1、添加网络备份功能，更新前请先备份数据!");
        presenter.checkApkVersion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //给标题栏的按钮添加监听事件
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.frame_layout_activity_home, targetFragment)
                    .commit();
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commit();
        }
        currentFragment = targetFragment;
    }

    @Override
    public void setQueryResult(String[] strings) {
        MyLog.info("setQueryResult 刷新widget");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("month", strings[0]);
        bundle.putString("day", strings[1]);
        bundle.putString("monthIn", strings[2]);
        bundle.putString("monthOut", strings[3]);
        bundle.putString("dayIn", strings[4]);
        bundle.putString("dayOut", strings[5]);
        intent.putExtra("info", bundle);
        intent.setAction(UPDATE_ACTION);
        MyLog.info("HomeActivity 广播准备发送");
        mContext.sendBroadcast(intent);
    }

    //show info activity
    @Override
    public void startUpdateActivity(String serverVersion, String messageBody) {
        Intent intent = new Intent(mContext, UpdateActivity.class);
        intent.putExtra("serverVersion", serverVersion);
        intent.putExtra("messageBody", messageBody);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.query();
    }
}
