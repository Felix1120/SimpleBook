package com.felix.simplebook.presenter;

import android.content.Context;
import android.widget.Toast;

import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.model.IRegisterModel;
import com.felix.simplebook.model.RegisterModel;
import com.felix.simplebook.utils.MyToast;
import com.felix.simplebook.view.IRegisterView;

/**
 * Created by chaofei.xue on 2018/6/8.
 */

public class RegisterPresenter implements IRegisterPresenter {

    private IRegisterModel registerModel;
    private IRegisterView registerView;
    private Context mContext;

    public RegisterPresenter(IRegisterView registerView, Context context){
        this.mContext = context;
        this.registerView = registerView;
        registerModel = new RegisterModel();
    }
    @Override
    public void register(String username, String password, String rePassword, String email,
                         String phone, String photos) {
        if(username==null||username.equals("")){
            MyToast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password==null||password.equals("")){
            MyToast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(email==null||email.equals("")){
            MyToast.makeText(mContext, "请输入邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone==null||phone.equals("")){
            MyToast.makeText(mContext, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(rePassword)){
            MyToast.makeText(mContext, "两次密码不相同", Toast.LENGTH_SHORT).show();
            return;
        }
        registerModel.Register(new ICallBack<String>() {
            @Override
            public void successful(String s) {

            }

            @Override
            public void error(String value) {

            }
        }, username, password, email, phone, username);
    }
}
