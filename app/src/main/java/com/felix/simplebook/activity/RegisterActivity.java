package com.felix.simplebook.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.presenter.IRegisterPresenter;
import com.felix.simplebook.presenter.RegisterPresenter;
import com.felix.simplebook.view.IRegisterView;

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
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.register(etUserName.getText().toString(), etPassword.getText().toString(),
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
}
