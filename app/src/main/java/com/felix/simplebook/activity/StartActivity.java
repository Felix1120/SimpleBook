package com.felix.simplebook.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.utils.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StartActivity extends Activity {
    @BindView(R.id.btn_ok_activity_start)
    Button btnOk;
    @BindView(R.id.et_password_activity_start)
    EditText etPassword;

    private String password;
    private SharedPreferences preferences;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        bind = ButterKnife.bind(StartActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPassword.getText().toString().trim().equals(password)) {
                    startActivity(new Intent(StartActivity.this,
                            HomeActivity.class));
                    finish();
                } else {
                    MyToast.makeText(StartActivity.this, "密码输入错误", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        preferences = getSharedPreferences("config.sb",
                                Context.MODE_PRIVATE);
                        password = preferences.getString("password", "no_password");
                        if (password.equals("no_password")) {
                            startActivity(new Intent(StartActivity.this,
                                    HomeActivity.class));
                            finish();
                        } else {
                            etPassword.setVisibility(View.VISIBLE);
                            btnOk.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
