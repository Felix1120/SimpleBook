package com.felix.simplebook.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.felix.simplebook.activity.MyCenterActivity;
import com.felix.simplebook.model.IMyCenterModel;
import com.felix.simplebook.model.MyCenterModel;
import com.felix.simplebook.view.IMyCenterView;

/**
 * Created by Felix on 2018/6/5.
 */

public class MyCenterPresenter implements IMyCenterPresenter {

    private IMyCenterView centerView;
    private IMyCenterModel centerModel;
    private Context context;

    public MyCenterPresenter(IMyCenterView centerView, Context context){
        this.centerView = centerView;
        this.context = context;
        centerModel = new MyCenterModel();
    }

    @Override
    public boolean isLogin() {
        SharedPreferences preferences = context.getSharedPreferences("config.sb",
                Context.MODE_PRIVATE);
        String userName = preferences.getString("isLogin", "noLogin");
        if(userName.equals("noLogin")){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public boolean exitLogin() {
        //清除登录信息
        SharedPreferences preferences = context.getSharedPreferences("config.sb",
                Context.MODE_PRIVATE);
        boolean commit = preferences.edit()
                .putString("username", "未登录")
                .putString("email", "登录后才能使用网络备份")
                .putString("photos", "")
                .putString("isLogin", "noLogin")
                .commit();
        return commit;
    }
}
