package com.felix.simplebook.model;


import com.felix.simplebook.callback.ICallBack;

import io.reactivex.Observer;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public interface IHomeModel {
    void queryData(ICallBack<String[]> callBack, String year, String month, String day);
    void checkApkVersion(Observer<String> observer);
}
