package com.felix.simplebook.model;

import android.content.Context;

import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.database.TypeBean;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public interface IHomeModel {
    void queryData(ICallBack<String[]> callBack, String year, String month, String day);
}
