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
            loginView.showMessage("用户名不能为空");
            return;
        }
        if (password.equals("")) {
            loginView.showMessage("密码不能为空");
            return;
        }
        loginModel.Login(new ICallBack<String>() {
            @Override
            public void successful(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if(object.get("result").equals("fail")){
                        loginView.showMessage("用户名或密码错误");
                    }else if(object.get("result").equals("successful")){
                        //保存登录信息
                        SharedPreferences preferences = context.getSharedPreferences("config.sb",
                                Context.MODE_PRIVATE);
                        preferences.edit()
                                .putString("username", object.get("user_name").toString())
                                .putString("email", object.get("email").toString())
                                .putString("photos", object.get("photos").toString())
                                .commit();
                        loginView.showMessage("登录成功");
                        loginView.close();
                        MyCenterActivity.startMyActivity(context, "login");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loginView.showMessage("服务器返回值错误");
                }
            }

            @Override
            public void error(String value) {
                loginView.showMessage("服务器请求错误");
            }
        }, username, password);
    }
}
