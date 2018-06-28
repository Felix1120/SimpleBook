package com.felix.simplebook.presenter;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.felix.simplebook.R;
import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.model.BackupNetModel;
import com.felix.simplebook.model.IBackupNetModel;
import com.felix.simplebook.utils.AutoBackUp;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.view.IBackupNetView;

import org.json.JSONObject;

/**
 * Created by chaofei.xue on 2018/6/28.
 */

public class BackupNetPresenter implements IBackupNetPresenter {

    private IBackupNetModel model;
    private IBackupNetView view;
    private Context mContext;
    private SharedPreferences preferences;
    private String username;

    public BackupNetPresenter(IBackupNetView view, Context mContext) {
        this.view = view;
        model = new BackupNetModel();
        this.mContext = mContext;
        preferences = mContext.getSharedPreferences("config.sb",
                Context.MODE_PRIVATE);
        username = preferences.getString("username", "");
    }

    @Override
    public void getTime() {
        model.getTime(new ICallBack<String>() {
            @Override
            public void successful(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String restoreTime = object.getString("restore_time");
                    String backupTime = object.getString("backup_time");
                    view.setTime(backupTime, restoreTime);
                } catch (Exception e) {

                }
            }

            @Override
            public void error(String value) {

            }
        }, username);
    }

    @Override
    public void backup() {
        view.showLoading();
        new AutoBackUp().startBackup(new ICallBack<Boolean>() {
            @Override
            public void successful(Boolean result) {
                MyLog.info("local save result",result+"");
                if (result) {
                    String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String filePath = sdPath + "/SimpleBook/" + username + ".bu";
                    model.backup(new ICallBack<String>() {
                        @Override
                        public void successful(String s) {
                            try {
                                JSONObject object = new JSONObject(s);
                                String backupResult = object.getString("result");
                                if("successful".equals(backupResult)){
                                    view.showMessage(mContext.getResources()
                                            .getString(R.string.backup_success));
                                }else{
                                    view.showMessage(mContext.getResources()
                                            .getString(R.string.backup_success));
                                }
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void error(String value) {

                        }
                    }, filePath);
                }
            }

            @Override
            public void error(String value) {

            }
        }, username);
    }

    @Override
    public void restore() {

    }
}
