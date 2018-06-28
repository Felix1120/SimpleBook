package com.felix.simplebook.model;

import com.felix.simplebook.callback.ICallBack;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chaofei.xue on 2018/6/28.
 */

public class BackupNetModel implements IBackupNetModel {
    @Override
    public void getTime(final ICallBack<String> callBack, String username) {
        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("username", username)
                .build();

        Request request = new Request.Builder()
                .url("")
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.error("亲，没网络我找不到服务器啊");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.successful(response.body().string());
            }
        });
    }

    @Override
    public void backup(final ICallBack<String> callBack, String filePath) {
        OkHttpClient client = new OkHttpClient();

        MultipartBody build = new MultipartBody
                .Builder()
                .addFormDataPart("image", filePath, RequestBody
                        .create(MediaType.parse("image/jpeg"), new File(filePath)))
                .build();


        Request request = new Request.Builder()
                .url("http://120.78.138.94:8080/server/BackUp")
                .post(build)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.error("");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.successful(response.body().string());
            }
        });
    }

    @Override
    public void restore(ICallBack<String> callBack, String username) {

    }
}
