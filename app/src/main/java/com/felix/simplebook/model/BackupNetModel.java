package com.felix.simplebook.model;

import android.database.Cursor;

import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.utils.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

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
                .url("http://47.106.219.34:8080/jz_server/GetTime")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.error("亲，没网络我找不到服务器啊");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                callBack.successful(result);
                MyLog.info("response", result);
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
    public void restore(final ICallBack<String> callBack, String username) {
        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("username", username + ".bu")
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
                try {
                    int restoreNumber = 0;
                    int noRestoreNumber = 0;
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        long id = object.getLong("id");
                        String year = object.getString("year");
                        String month = object.getString("month");
                        String day = object.getString("day");
                        String time = object.getString("time");
                        String money = object.getString("money");
                        String inOrOut = object.getString("in_or_out");
                        String type = object.getString("type");
                        String status = object.getString("status");
                        InfoBean infoBean = new InfoBean(id, year, month, day, time, type, money, status,
                                inOrOut);
                        String sql = "select * from InfoBean where year = ? and month = ? and day = ?" +
                                "and time = ? and money = ? and inorout = ? and type = ? and status = ?";
                        Cursor cursor = DataSupport.findBySQL(sql, year, month, day, time, money, inOrOut, type, status);
                        if (!cursor.moveToNext()) {
                            if (infoBean.save()) {
                                restoreNumber++;
                            } else {

                            }
                        } else {
                            noRestoreNumber++;
                        }
                        //置空
                        infoBean = null;
                    }
                    callBack.successful(restoreNumber + "条记录还原完成，" + noRestoreNumber + "条记录重复未还原");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
