package com.felix.simplebook.presenter;

import android.content.Context;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.model.IRegisterModel;
import com.felix.simplebook.model.RegisterModel;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.MyToast;
import com.felix.simplebook.view.IRegisterView;

import org.json.JSONObject;

import java.util.regex.Pattern;

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
    public void register(String code, String username, String password, String rePassword, String email,
                         String phone, String photos) {
        // null check
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
        if (code == null || code.equals("")) {
            MyToast.makeText(mContext, mContext.getResources().getString(R.string.re_code_show),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // format check
        if (code.length() != 6) {
            MyToast.makeText(mContext, mContext.getResources().
                    getString(R.string.re_code_format_fail_show), Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.length() < 5 || username.length() > 15) {
            MyToast.makeText(mContext, mContext.getResources().
                    getString(R.string.re_username_format_fail_show), Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            MyToast.makeText(mContext, mContext.getResources().
                    getString(R.string.re_password_format_fail_show), Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() > 15) {
            MyToast.makeText(mContext, mContext.getResources().
                    getString(R.string.re_password_format_fail2_show), Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.length() != 11 || !checkPhone(phone)) {
            MyToast.makeText(mContext, mContext.getResources().
                    getString(R.string.re_phone_format_fail_show), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkEmail(email)) {
            MyToast.makeText(mContext, mContext.getResources().
                    getString(R.string.re_email_format_fail_show), Toast.LENGTH_SHORT).show();
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
                    } else if(object.get("result").equals("code_error")){
                        registerView.showMessage(mContext.getResources()
                                .getString(R.string.re_error_code_show));
                    } else if(object.get("result").equals("code_fail")){
                        registerView.showMessage(mContext.getResources()
                                .getString(R.string.re_fail_code_show));
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
        }, code, username, password, email, phone, username);
    }

    private boolean checkPhone(String number) {
        char one = number.charAt(0);
        char two = number.charAt(1);
        MyLog.info(one + "," +two);
        if (one == '1') {
            if (two != '0' || two != '1' || two != '2' || two != '9') {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean checkEmail(String email){
        String check = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return Pattern.matches(check, email);
    }
}
