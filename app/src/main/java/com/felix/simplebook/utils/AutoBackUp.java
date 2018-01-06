package com.felix.simplebook.utils;

import android.os.Environment;

import com.felix.simplebook.database.InfoBean;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by chaofei.xue on 2018/1/6.
 */

public class AutoBackUp {

    public void startBackup() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    MyLog.info("path:"+path);
                    File files = new File(path+"/简账");
                    if(!files.exists()){
                        files.mkdirs();
                    }
                    File file = new File(files.toString(), "自动备份.bu");
                    final FileOutputStream fos = new FileOutputStream(file);
                    List<InfoBean> infoBeans = DataSupport.findAll(InfoBean.class);
                    JSONObject json = new JSONObject();
                    JSONArray array = new JSONArray();
                    for (InfoBean infoBean : infoBeans) {
                        JSONObject object = new JSONObject();
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
                } catch (Exception e) {
                    e.printStackTrace();
                    MyLog.info(e.toString());
                }
            }
        }.start();
    }
}
