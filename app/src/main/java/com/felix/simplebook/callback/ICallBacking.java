package com.felix.simplebook.callback;

/**
 * Created by chaofei.xue on 2017/12/11.
 */

public interface ICallBacking<T> {
    void successful(T t);
    void error(String value);
    void updateInfo(T t);
}
