package com.felix.simplebook.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.felix.simplebook.R;
import com.felix.simplebook.activity.LoginActivity;
import com.felix.simplebook.activity.MyCenterActivity;
import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.TypeBean;
import com.felix.simplebook.model.HomeModel;
import com.felix.simplebook.model.IHomeModel;
import com.felix.simplebook.model.ILoginModel;
import com.felix.simplebook.model.LoginModel;
import com.felix.simplebook.utils.GetTime;
import com.felix.simplebook.view.IHomeView;
import com.felix.simplebook.view.ILoginView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chaofei.xue on 2018/1/2.
 */

public class LoginPresenter implements ILoginPresenter {
    private ILoginView loginView;
    private ILoginModel loginModel;
    private Context context;

    public LoginPresenter(ILoginView loginView, Context context) {
        this.context = context;
        this.loginView = loginView;
        loginModel = new LoginModel();
    }

    @Override
    public void login(String username, String password) {
        if (username.equals("")) {
            loginView.showMessage(context.getResources()
                    .getString(R.string.login_username_show));
            return;
        }
        if (password.equals("")) {
            loginView.showMessage(context.getResources()
                    .getString(R.string.login_password_show));
            return;
        }
        loginView.showLoading();
        loginModel.Login(new ICallBack<String>() {
            @Override
            public void successful(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if(object.get("result").equals("fail")){
                        loginView.showMessage(context.getResources()
                                .getString(R.string.login_fail_show));
                    }else if(object.get("result").equals("successful")){
                        //保存登录信息
                        SharedPreferences preferences = context.getSharedPreferences("config.sb",
                                Context.MODE_PRIVATE);
                        preferences.edit()
                                .putString("username", object.get("user_name").toString())
                                .putString("email", object.get("email").toString())
                                .putString("photos", object.get("user_name").toString() + ".jpg")
                                .putString("isLogin","Login")
                                .commit();
                        loginView.showMessage(context.getResources()
                                .getString(R.string.login_success_show));
                        loginView.close();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loginView.showMessage(context.getResources()
                            .getString(R.string.login_error_show));
                }
            }

            @Override
            public void error(String value) {
                loginView.showMessage(context.getResources()
                        .getString(R.string.login_no_inter_show));
            }
        }, username, password);
    }
}
