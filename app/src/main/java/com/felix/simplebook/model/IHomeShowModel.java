package com.felix.simplebook.model;

import android.content.Context;

import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.database.TypeBean;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public interface IHomeShowModel {
    void saveDataRequest(ICallBack<InfoBean> mCallBack, InfoBean mInfoBean, String flag);
    void readType(ICallBack<List<TypeBean>> mCallBack, Context context);
    void writeType(ICallBack<List<TypeBean>> mCallBack, TypeBean typeBean);
}
