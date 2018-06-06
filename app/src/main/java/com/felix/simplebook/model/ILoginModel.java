package com.felix.simplebook.model;

import com.felix.simplebook.callback.ICallBack;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public interface ILoginModel {
    void Login(ICallBack<String> callBack, String username, String password);
}
