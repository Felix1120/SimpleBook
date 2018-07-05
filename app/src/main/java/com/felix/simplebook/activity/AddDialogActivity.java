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

import java.util.List;

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
                if(etType.getText().toString().trim().equals("")){
                    MyToast.makeText(AddDialogActivity.this, "请输入类型",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (typeBean != null) {
                    List<TypeBean> list = DataSupport.where("type = ?", etType.getText().toString().trim())
                            .find(TypeBean.class);
                    if (list.size() > 0) {
                        MyToast.makeText(AddDialogActivity.this, "该类型已存在，修改失败",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
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
                    List<TypeBean> list = DataSupport.where("type = ?", etType.getText().toString().trim())
                            .find(TypeBean.class);
                    if (list.size() > 0) {
                        MyToast.makeText(AddDialogActivity.this, "该类型已存在，添加失败",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    TypeBean mTypeBean = new TypeBean(etType.getText().toString().trim());
                    if (mTypeBean.save()) {
                        MyToast.makeText(AddDialogActivity.this, "添加成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(AddFragment.UPDATE_ADD_FRAGMENT);
                        sendBroadcast(intent);
                        finish();
                    } else {
                        MyToast.makeText(AddDialogActivity.this, "添加失败",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
