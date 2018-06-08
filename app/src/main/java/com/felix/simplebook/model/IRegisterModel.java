package com.felix.simplebook.model;

import com.felix.simplebook.callback.ICallBack;

/**
 * Created by chaofei.xue on 2018/6/8.
 */

public interface IRegisterModel {
    void Register(ICallBack<String> callBack, String username, String password, String email,
                  String phone, String photos);
}
