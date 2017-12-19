package com.felix.simplebook.model;

import android.database.Cursor;

import com.felix.simplebook.callback.ICallBacking;
import com.felix.simplebook.database.InfoBean;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by chaofei.xue on 2017/12/11.
 */

public class BackUpModel implements IBackUpModel {
    @Override
    public void databaseToFile(final ICallBacking<String> callBack, final String... path) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    File file = new File(path[0], path[1] + ".bu");
                    if (file.exists()) {
                        callBack.error("文件已存在");
                        callBack.error("备份失败");
                        return;
                    }
                    final FileOutputStream fos = new FileOutputStream(file);
                    List<InfoBean> infoBeans = DataSupport.findAll(InfoBean.class);
                    JSONObject json = new JSONObject();
                    JSONArray array = new JSONArray();
                    for (InfoBean infoBean : infoBeans) {
                        JSONObject object = new JSONObject();
                        String info = infoBean.getTime() + " "
                                + infoBean.getType() + " " + infoBean.getMoney() + " "
                                + infoBean.getStatus();
                        callBack.updateInfo(info);
                        object.put("id", infoBean.getId());
                        object.put("year", infoBean.getYear());
                        object.put("month", infoBean.getMonth());
                        object.put("day", infoBean.getDay());
                        object.put("time", infoBean.getTime());
                        object.put("money", infoBean.getMoney());
                        object.put("in_or_out", infoBean.getInOrOut());
                        object.put("type", infoBean.getType());
                        object.put("status", infoBean.getStatus());
                        array.put(object);
                    }
                    json.put("info", array);
                    String value = json.toString();
                    fos.write(value.getBytes());
                    fos.flush();
                    fos.close();
                    callBack.successful("备份完成");
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.error("备份失败");
                }
            }
        }.start();
    }

    @Override
    public void fileToDatabase(final ICallBacking<String> callBack, final String... path) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    File file = new File(path[0]);
                    if (!file.exists()) {
                        callBack.error("文件不存在");
                        callBack.error("还原失败");
                        return;
                    }
                    FileInputStream fis = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int number;
                    while ((number = fis.read(bytes)) != -1) {
                        stringBuffer.append(new String(bytes, 0, number));
                    }
                    fis.close();
                    JSONObject jsonObject = new JSONObject(stringBuffer.toString());
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
                                callBack.updateInfo("已还原 " + time + " " + type + " " + money + " " + status);
                            }else{

                            }
                        } else {
                            callBack.updateInfo("已存在 " + time + " " + type + " " + money + " " + status);
                        }
                        //置空
                        infoBean = null;
                    }
                    callBack.successful("还原完成");
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.error("还原失败");
                }
            }
        }.start();
    }
}
