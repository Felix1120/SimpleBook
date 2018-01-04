package com.felix.simplebook.activity;


import android.content.ContentValues;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.database.TypeBean;
import com.felix.simplebook.fragment.AddFragment;
import com.felix.simplebook.utils.MyToast;

import org.litepal.crud.DataSupport;

import butterknife.BindView;

public class AddDialogActivity extends BaseActivity {
    @BindView(R.id.img_ok_activity_add_dialog)
    ImageView imgOk;
    @BindView(R.id.et_type_activity_add_dialog)
    EditText etType;

    private TypeBean typeBean;
    private Intent intent;

    @Override
    public int initLayout() {
        return R.layout.activity_add_dialog;
    }

    @Override
    public void initView() {
        setFinishOnTouchOutside(true);
        intent = getIntent();
        typeBean = (TypeBean) intent.getSerializableExtra("info");
        if (typeBean != null) {
            etType.setText(typeBean.getType());
        }
    }

    @Override
    public void initData() {
        imgOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeBean != null) {
                    long id = typeBean.getId();
                    ContentValues values = new ContentValues();
                    values.put("type", etType.getText().toString().trim());
                    int update = DataSupport.update(TypeBean.class, values, id);
                    if (update > 0) {
                        MyToast.makeText(AddDialogActivity.this, "修改成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(AddFragment.UPDATE_ADD_FRAGMENT);
                        sendBroadcast(intent);
                        finish();
                    } else {
                        MyToast.makeText(AddDialogActivity.this, "修改失败",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    TypeBean mTypeBean = new TypeBean(etType.getText().toString().trim());
                    if (mTypeBean.save()) {
                        MyToast.makeText(AddDialogActivity.this, "添加成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(AddFragment.UPDATE_ADD_FRAGMENT);
                        sendBroadcast(intent);
                        finish();
                    }else {
                        MyToast.makeText(AddDialogActivity.this, "添加失败",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
