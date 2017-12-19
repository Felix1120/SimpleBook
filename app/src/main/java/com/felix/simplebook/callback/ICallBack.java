package com.felix.simplebook.callback;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public interface ICallBack<T> {
    void successful(T t);
    void error(String value);
}
