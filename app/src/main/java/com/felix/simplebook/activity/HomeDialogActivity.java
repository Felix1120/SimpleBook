package com.felix.simplebook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseActivity;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.database.MyDataBase;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class HomeDialogActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.btn_back_activity_home_dialog)
    Button btnBack;
    @BindView(R.id.btn_del_activity_home_dialog)
    Button btnDel;
    @BindView(R.id.btn_edit_activity_home_dialog)
    Button btnEdit;

    private Unbinder bind;
    private Bundle bundle;
    private InfoBean infoBean;

    @Override
    public int initLayout() {
        return R.layout.activity_home_dialog;
    }

    @Override
    public void initView() {
        bind = ButterKnife.bind(HomeDialogActivity.this);
    }

    @Override
    public void initData() {
        btnBack.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        bundle = getIntent().getExtras();
        infoBean = (InfoBean) bundle.get("info");
        MyLog.info("HomeDialogActivity",((InfoBean)bundle.get("info")).getId()+"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back_activity_home_dialog:
                finish();
                break;
            case R.id.btn_del_activity_home_dialog:
                Intent intent = new Intent(HomeDialogActivity.this, DialogActivity.class);
                intent.putExtra("title", "提示");
                intent.putExtra("message", "确定要删除该记录吗？");
                startActivityForResult(intent, 0);
                break;
            case R.id.btn_edit_activity_home_dialog:
                Intent mIntent = new Intent(HomeDialogActivity.this, HomeShowActivity.class);
                mIntent.putExtras(bundle);
                mIntent.putExtra("flag","HomeDialogActivity");
                startActivity(mIntent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            MyDataBase dataBase = MyDataBase.getInstance();
            dataBase.execute(MyDataBase.DELETE, infoBean, new Observer<Boolean>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (aBoolean){
                        MyToast.makeText(HomeDialogActivity.this, "删除成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("com.felix.simplebook.successful");
                        intent.putExtra("what", "init");
                        mContext.sendBroadcast(intent);
                    }else{
                        MyToast.makeText(HomeDialogActivity.this, "删除失败",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
            finish();
        }
    }
}
