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
 * Created by chaofei.xue on 2018/1/2.
 */

public class LoginModel implements ILoginModel {

    @Override
    public void Login(final ICallBack<String> callBack, String username, String password) {
        try {
            OkHttpClient client = new OkHttpClient();
            FormBody body = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url("http://47.106.219.34:8080/jz_server/Login")
                    .post(body)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callBack.error("error");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    callBack.successful(response.body().string());
                    MyLog.info("Felix","LoginModel",response.body().toString());
                }
            });
        } catch (Exception e) {

        }
    }
}
