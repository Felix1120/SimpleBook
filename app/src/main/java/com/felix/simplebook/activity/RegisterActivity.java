package com.felix.simplebook.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.presenter.IRegisterPresenter;
import com.felix.simplebook.presenter.RegisterPresenter;
import com.felix.simplebook.utils.MyToast;
import com.felix.simplebook.view.IRegisterView;

import java.lang.ref.WeakReference;

import butterknife.BindView;

public class RegisterActivity extends BaseActivity implements IRegisterView {

    @BindView(R.id.btn_activity_register)
    Button btnRegister;

    @BindView(R.id.username_activity_register)
    EditText etUserName;

    @BindView(R.id.password_activity_register)
    EditText etPassword;

    @BindView(R.id.re_password_activity_register)
    EditText etRePassword;

    @BindView(R.id.phone_activity_register)
    EditText etPhone;

    @BindView(R.id.email_activity_register)
    EditText etEmail;

    @BindView(R.id.tv_login_activity_register)
    TextView tvLogin;

    @BindView(R.id.toolbar_activity_register)
    Toolbar mToolbar;

    @BindView(R.id.img_loading_activity_register)
    ImageView imgLoading;

    @BindView(R.id.code_activity_register)
    EditText etCode;

    private MyHandler myHandler = new MyHandler(this);

    private class MyHandler extends Handler {
        WeakReference<Activity> weakReference;

        public MyHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyToast.makeText(weakReference.get(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
            closeLoading();

        }
    }

    private IRegisterPresenter presenter;
    @Override
    public int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        setSupportActionBar(mToolbar);
        presenter = new RegisterPresenter(RegisterActivity.this, mContext);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.register(etCode.getText().toString(),
                        etUserName.getText().toString(), etPassword.getText().toString(),
                        etRePassword.getText().toString(), etEmail.getText().toString(),
                        etPhone.getText().toString(), etUserName.getText().toString());
            }
        });

        //透明化状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void goLogin() {
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }

    @Override
    public void showMessage(String msg) {
        Message message = Message.obtain();
        message.obj = msg;
        myHandler.sendMessage(message);
    }

    @Override
    public void showLoading() {
        imgLoading.setVisibility(View.VISIBLE);
        btnRegister.setText("注       册       中");
        btnRegister.setEnabled(false);
        etCode.setEnabled(false);
        etPassword.setEnabled(false);
        etUserName.setEnabled(false);
        etRePassword.setEnabled(false);
        etPhone.setEnabled(false);
        etEmail.setEnabled(false);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.loading);
        imgLoading.startAnimation(animation);
        tvLogin.setEnabled(false);
        tvLogin.setTextColor(getResources().getColor(R.color.write_tran));
    }

    @Override
    public void closeLoading() {
        imgLoading.clearAnimation();
        imgLoading.setVisibility(View.GONE);
        btnRegister.setText("注                 册");
        btnRegister.setEnabled(true);
        etCode.setEnabled(true);
        etPassword.setEnabled(true);
        etUserName.setEnabled(true);
        etRePassword.setEnabled(true);
        etPhone.setEnabled(true);
        etEmail.setEnabled(true);
        tvLogin.setEnabled(true);
        tvLogin.setTextColor(getResources().getColor(R.color.write_2));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacks(null);
    }
}
