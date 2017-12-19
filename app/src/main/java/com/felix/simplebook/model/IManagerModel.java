package com.felix.simplebook.model;

import android.content.Context;

import com.felix.simplebook.bean.SimpleBean;
import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.InfoBean;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/28.
 */

public interface IManagerModel {
    void queryDataBase(int flag, String year,ICallBack<List<String>> callBack);
    void querySimple(String year, String month, ICallBack<List<SimpleBean>> callBack);
    void queryFull(String year, String month, ICallBack<List<InfoBean>> callBack);
}
