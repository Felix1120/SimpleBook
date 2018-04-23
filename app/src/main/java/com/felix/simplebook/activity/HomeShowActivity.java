package com.felix.simplebook.activity;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.callback.IOnClickListener;
import com.felix.simplebook.presenter.HomeShowPresenter;
import com.felix.simplebook.presenter.IHomeShowPresenter;
import com.felix.simplebook.utils.GetTime;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.MyToast;
import com.felix.simplebook.utils.SpinnerPopWindow;
import com.felix.simplebook.view.IHomeShowView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class HomeShowActivity extends BaseActivity implements IHomeShowView {

    @BindView(R.id.rg_activity_home_show)
    RadioGroup mGroup;
    @BindView(R.id.radio_out_activity_home_show)
    RadioButton outRb;
    @BindView(R.id.radio_in_activity_home_show)
    RadioButton inRb;
    @BindView(R.id.et_time_activity_home_show)
    EditText timeEt;
    @BindView(R.id.et_type_activity_home_show)
    EditText typeEt;
    @BindView(R.id.et_money_activity_home_show)
    EditText moneyEt;
    @BindView(R.id.et_status_activity_home_show)
    EditText statusEt;
    @BindView(R.id.btn_ok_activity_home_show)
    Button saveBtn;
    @BindView(R.id.btn_cancel_activity_home_show)
    Button cancelBtn;

    private IHomeShowPresenter mPresenter;
    private SpinnerPopWindow mSpinnerPopWindow;

    private Unbinder bind;

    @Override
    public int initLayout() {
        return R.layout.activity_home_show;
    }

    @Override
    public void initView() {
        bind = ButterKnife.bind(HomeShowActivity.this);
    }

    @Override
    public void initData() {
        mPresenter = new HomeShowPresenter(this, mContext);
        final RadioButton[] mRb = new RadioButton[1];
        outRb.setChecked(true);
        mRb[0] = outRb;
        timeEt.setText(GetTime.get());
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int item) {
                mRb[0] = group.findViewById(item);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inOrOut = "out";
                if (mRb[0].getId() == R.id.radio_in_activity_home_show) {
                    inOrOut = "in";
                }
                MyLog.info("HomeShowActivity", "onClick");
                mPresenter.saveData(inOrOut, timeEt.getText().toString(),
                        typeEt.getText().toString(), moneyEt.getText().toString(),
                        statusEt.getText().toString());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        mPresenter.setIntent(getIntent());
        mPresenter.getList();
    }

    @Override
    public void showMessage(String msg) {
        MyLog.info("HomeShowActivity", "showMessage");
        MyToast.makeText(HomeShowActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cancel() {
        finish();
    }

    @Override
    public void setData(String inOrOut, String time, String type, String money, String status) {
        if (inOrOut.equals("in")) {
            inRb.setChecked(true);
        } else {
            outRb.setChecked(true);
        }
        timeEt.setText(time);
        typeEt.setText(type);
        moneyEt.setText(money);
        statusEt.setText(status);
    }

    @Override
    public void setSpinner(List<String> list) {
        mSpinnerPopWindow = new SpinnerPopWindow(mContext, list, clickListener);
        mSpinnerPopWindow.setOnDismissListener(dismissListener);
        typeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpinnerPopWindow.setWidth(typeEt.getWidth());
                mSpinnerPopWindow.setHeight(300);
                mSpinnerPopWindow.showAsDropDown(typeEt);
                setTextImage(R.drawable.spinner_up);
            }
        });
    }

    @Override
    public void setType(String type) {
        typeEt.setText(type);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    private IOnClickListener clickListener = new IOnClickListener() {
        @Override
        public void onClick(String value) {
            MyLog.info("获取的数据:"+value);
            typeEt.setText(value);
            mSpinnerPopWindow.dismiss();
        }
    };

    private PopupWindow.OnDismissListener dismissListener=new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setTextImage(R.drawable.spinner_down);
        }
    };

    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
        typeEt.setCompoundDrawables(null, null, drawable, null);
    }
}