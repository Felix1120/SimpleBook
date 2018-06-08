package com.felix.simplebook.model;

import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.utils.MyLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chaofei.xue on 2018/6/8.
 */

public class RegisterModel implements IRegisterModel {
    @Override
    public void Register(final ICallBack<String> callBack, String username, String password,
                         String email, String phone, String photos) {
        try {
            OkHttpClient client = new OkHttpClient();
            FormBody body = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .add("email", email)
                    .add("phone", phone)
                    .add("photos", photos)
                    .build();
            Request request = new Request.Builder()
                    .url("http://120.78.138.94:8080/server/Register")
                    .post(body)
                    .build();
            Call mCall = client.newCall(request);
            mCall.enqueue(new Callback() {
                              @Override
                              public void onFailure(Call call, IOException e) {
                                  callBack.error("服务器连接失败");
                              }

                              @Override
                              public void onResponse(Call call, Response response) throws IOException {
                                  callBack.successful(response.body().string());
                                  MyLog.info("RM:", response.body().string());
                              }
                          }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
