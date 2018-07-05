package com.felix.simplebook.view;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public interface IHomeView {
    void setQueryResult(String[] strings);
    void startUpdateActivity(String serverVersion, String messageBody);
}
