package com.felix.simplebook.presenter;

import android.content.Context;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.model.IRegisterModel;
import com.felix.simplebook.model.RegisterModel;
import com.felix.simplebook.utils.MyToast;
import com.felix.simplebook.view.IRegisterView;

import org.json.JSONObject;

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
            MyToast.makeText(mContext, mContext.getResources().getString(R.string.re_username_show),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(password==null||password.equals("")){
            MyToast.makeText(mContext, mContext.getResources().getString(R.string.re_password_show)
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        if(email==null||email.equals("")){
            MyToast.makeText(mContext, mContext.getResources().getString(R.string.re_email_show),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone==null||phone.equals("")){
            MyToast.makeText(mContext, mContext.getResources().getString(R.string.re_phone_show),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(rePassword)){
            MyToast.makeText(mContext, mContext.getResources().getString(R.string.re_passwords_show),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        registerView.showLoading();
        registerModel.Register(new ICallBack<String>() {
            @Override
            public void successful(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.get("result").equals("error")) {
                        registerView.showMessage(mContext.getResources()
                                .getString(R.string.re_error_show));
                    } else if (object.get("result").equals("fail")) {
                        registerView.showMessage(mContext.getResources()
                                .getString(R.string.re_fail_show));
                    } else if (object.get("result").equals("successful")) {
                        registerView.showMessage(mContext.getResources()
                                .getString(R.string.re_success_show));
                        registerView.goLogin();
                    }
                } catch (Exception e) {
                    registerView.showMessage(mContext.getResources()
                            .getString(R.string.re_fail_show));
                }
            }

            @Override
            public void error(String value) {
                registerView.showMessage(value);
            }
        }, username, password, email, phone, username);
    }
}
