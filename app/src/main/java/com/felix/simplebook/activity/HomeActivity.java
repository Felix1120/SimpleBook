package com.felix.simplebook.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.FrameLayout;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.fragment.BackupFragment;
import com.felix.simplebook.fragment.HomeFragment;
import com.felix.simplebook.fragment.ManagerFragment;
import com.felix.simplebook.view.IHomeView;

import butterknife.BindView;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class HomeActivity extends BaseActivity implements IHomeView {

    @BindView(R.id.toolbar_activity_home)
    Toolbar mToolbar;

    @BindView(R.id.dl_activity_home)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.fab_activity_home)
    FloatingActionButton mActionButton;

    @BindView(R.id.frame_layout_activity_home)
    FrameLayout mFrameLayout;

    @BindView(R.id.nav_activity_home)
    NavigationView mNavigationView;

    private Fragment currentFragment;
    private ManagerFragment managerFragment;
    private HomeFragment homeFragment;
    private BackupFragment backupFragment;

    @Override
    public int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        homeFragment = new HomeFragment();
        managerFragment = new ManagerFragment();
        backupFragment = new BackupFragment();

        //设置默认界面
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_activity_home, homeFragment)
                .commit();
        currentFragment = homeFragment;
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
                    case R.id.lock:
                        startActivity(new Intent(HomeActivity.this,
                                LockActivity.class));
                        break;
                    case R.id.backup:
                        mActionButton.setVisibility(View.GONE);
                        switchFragment(backupFragment);
                        break;
                    case R.id.about:
                        startActivity(new Intent(HomeActivity.this,
                                AboutActivity.class));
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

        if(ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void show() {

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar, menu);
//        return true;
//    }

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
        }// else if (item.getItemId() == R.id.score) {
//            Toast.makeText(mContext, "满分好评", Toast.LENGTH_SHORT).show();
//        }
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
}
