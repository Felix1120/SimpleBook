package com.felix.simplebook.activity;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class DialogActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_title_activity_dialog)
    TextView tvTitle;
    @BindView(R.id.tv_message_activity_dialog)
    TextView tvMsg;
    @BindView(R.id.btn_ok_activity_dialog)
    Button btnOk;
    @BindView(R.id.btn_cancel_activity_dialog)
    Button btnCancel;

    private  Unbinder bind;

    @Override
    public int initLayout() {
        return R.layout.activity_dialog;
    }

    @Override
    public void initView() {
        bind = ButterKnife.bind(DialogActivity.this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        tvTitle.setText(title);
        tvMsg.setText(message);

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel_activity_dialog:
                finish();
                break;
            case R.id.btn_ok_activity_dialog:
                setResult(1);
                finish();
                break;
        }
    }
}
