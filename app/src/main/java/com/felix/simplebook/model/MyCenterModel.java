package com.felix.simplebook.model;

import com.felix.simplebook.callback.ICallBack;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Felix on 2018/6/5.
 */

public class MyCenterModel implements IMyCenterModel {

    @Override
    public void updateImg(final ICallBack<String> callBack, String imagePath) {
        OkHttpClient client = new OkHttpClient();

        MultipartBody build = new MultipartBody
                .Builder()
                .addFormDataPart("image", imagePath, RequestBody
                        .create(MediaType.parse("image/jpeg"), new File(imagePath)))
                .build();


        Request request = new Request.Builder()
                .url("http://47.106.219.34:8080/jz_server/BackUp")
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
}
