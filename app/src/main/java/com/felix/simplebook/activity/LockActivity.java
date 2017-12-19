package com.felix.simplebook.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.utils.MyToast;
import butterknife.BindView;

public class LockActivity extends BaseActivity {
    @BindView(R.id.et_old_password_lock_activity)
    EditText etOldPassword;
    @BindView(R.id.et_new_password_lock_activity)
    EditText etNewPassword;
    @BindView(R.id.et_password_lock_activity)
    EditText etPassword;
    @BindView(R.id.btn_ok_activity_lock)
    Button btnOk;
    @BindView(R.id.btn_cancel_activity_lock)
    Button btnCancel;
    @BindView(R.id.ll_button_activity_lock)
    LinearLayout llBtn;
    @BindView(R.id.btn_update_activity_lock)
    Button btnUpdate;
    @BindView(R.id.btn_close_activity_lock)
    Button btnClose;

    private String password;
    private SharedPreferences preferences;
    private int count;
    private int status;

    @Override
    public int initLayout() {
        return R.layout.activity_lock;
    }

    @Override
    public void initView() {
        etOldPassword.setVisibility(View.GONE);
        etNewPassword.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        llBtn.setVisibility(View.GONE);

        preferences = getSharedPreferences("config.sb",
                Context.MODE_PRIVATE);
        password = preferences.getString("password", "no_password");

        //判断用户是否加密，未加密跳转到下面执行
        if(password.equals("no_password")){
            llBtn.setVisibility(View.VISIBLE);
            etNewPassword.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            etOldPassword.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
            btnClose.setVisibility(View.GONE);
            return;
        }

        //用户已加密分支
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNewPassword.setVisibility(View.GONE);
                etPassword.setVisibility(View.GONE);
                llBtn.setVisibility(View.VISIBLE);
                etOldPassword.setVisibility(View.VISIBLE);
                btnUpdate.setVisibility(View.GONE);
                btnClose.setVisibility(View.GONE);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 100;
                etNewPassword.setVisibility(View.GONE);
                etPassword.setVisibility(View.GONE);
                llBtn.setVisibility(View.VISIBLE);
                etOldPassword.setVisibility(View.VISIBLE);
                btnUpdate.setVisibility(View.GONE);
                btnClose.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void initData() {
        setFinishOnTouchOutside(true);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.equals("no_password")) {
                    setLock();
                }else{
                    String oldPassword = etOldPassword.getText().toString().trim();
                    if(oldPassword.equals(password)){
                        if(status == 100){
                            preferences.edit().putString("password", "no_password").commit();
                            MyToast.makeText(LockActivity.this, "密码已关闭", Toast.LENGTH_SHORT)
                                    .show();
                            finish();
                            return;
                        }
                        etOldPassword.setVisibility(View.GONE);
                        etNewPassword.setVisibility(View.VISIBLE);
                        etPassword.setVisibility(View.VISIBLE);
                        count++;
                        setLock();
                    }else{
                        MyToast.makeText(LockActivity.this, "密码验证失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    public void setLock(){
        String password_one = etPassword.getText().toString().trim();
        String password_two = etNewPassword.getText().toString().trim();
        if(count == 1){
            return;
        }
        if(password_one.equals(password)){
            MyToast.makeText(LockActivity.this, "不能和原密码相同", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password_two.equals(null)||password_two.equals("")){
            MyToast.makeText(LockActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password_two.equals(password_one)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("password", password_one)
                    .commit();
            MyToast.makeText(LockActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            MyToast.makeText(LockActivity.this, "两次输入不相同", Toast.LENGTH_SHORT).show();
        }
    }

}
