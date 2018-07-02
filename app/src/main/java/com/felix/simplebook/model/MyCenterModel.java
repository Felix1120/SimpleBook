package com.felix.simplebook.model;

import android.content.Context;
import android.os.Environment;

import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.NetInfoType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
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

    @Override
    public void downloadImg(final ICallBack<File> callBack, final String imagePath) {
        checkFileInfo(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    File fileDir = new File(Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/SimpleBook");
                    JSONObject object = new JSONObject(response.body().string());
                    String result = object.getString("result");
                    if ("no".equals(result)) {
                        callBack.error("no photos");
                        MyLog.info("service no photos, loading local color...");
                    } else if (!fileDir.exists()) {
                        download(callBack, imagePath);
                        MyLog.info("file not found, go to service download photos...");
                    } else if (fileDir.exists()) {
                        File file = new File(fileDir, imagePath);
                        String length = String.valueOf(file.length());
                        if (length.equals(result)) {
                            MyLog.info("file no changes, loading local photos...");
                            callBack.successful(file);
                        } else {
                            MyLog.info("file changes, go to service download photos...");
                            download(callBack, imagePath);
                        }
                    }

                } catch (JSONException e) {

                }
            }
        }, imagePath);
    }

    private void checkFileInfo(Callback callback, String fileName) {
        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("fileName", fileName)
                .build();

        Request request = new Request.Builder()
                .url("http://47.106.219.34:8080/jz_server/CheckFileInfo")
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    private void download(final ICallBack<File> callBack, final String imagePath) {
        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("username", imagePath)
                .build();

        Request request = new Request.Builder()
                .url("http://47.106.219.34:8080/jz_server/Restore")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SimpleBook");
                if (!path.exists()) {
                    path.mkdirs();
                }
                File file = new File(path, imagePath);

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(response.body().bytes());
                fos.flush();
                fos.close();
                callBack.successful(file);
            }
        });
    }
}