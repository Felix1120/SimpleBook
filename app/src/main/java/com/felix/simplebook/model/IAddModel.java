package com.felix.simplebook.model;

import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.TypeBean;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public interface IAddModel {
    void queryData(ICallBack<List<TypeBean>> callBack);
}
