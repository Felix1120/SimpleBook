package com.felix.simplebook.model;


import com.felix.simplebook.callback.ICallBack;

/**
 * Created by chaofei.xue on 2017/12/11.
 */

public interface IBackupNetModel {
    void getTime(ICallBack<String> callBack, String username);
    void backup(ICallBack<String> callBack, String filePath);
    void restore(ICallBack<String> callBack, String username);
}
