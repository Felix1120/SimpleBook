package com.felix.simplebook.activity;


import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.presenter.LoginPresenter;
import com.felix.simplebook.utils.MyToast;
import com.felix.simplebook.view.ILoginView;


import java.lang.ref.WeakReference;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements ILoginView {

    @BindView(R.id.btn_activity_login)
    Button btnLogin;

    @BindView(R.id.username_activity_login)
    EditText etUserName;

    @BindView(R.id.password_activity_login)
    EditText etPassword;

    @BindView(R.id.register_activity_login)
    TextView tvRegister;

    @BindView(R.id.toolbar_activity_login)
    Toolbar mToolbar;

    private LoginPresenter loginPresenter;

    private MyHandler myHandler = new MyHandler(this);

    private Intent mIntent;

    //弱引用handler
    private class MyHandler extends Handler {
        WeakReference<Activity> weakReference;

        public MyHandler(Activity activity) {
            weakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyToast.makeText(weakReference.get(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        setSupportActionBar(mToolbar);

        loginPresenter = new LoginPresenter(this, mContext);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(mIntent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.login(etUserName.getText().toString(),
                        etPassword.getText().toString());
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
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMessage(String msg) {
        Message message = Message.obtain();
        message.obj = msg;
        myHandler.sendMessage(message);
    }

    @Override
    public void close() {
        finish();
    }
}
