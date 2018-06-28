package com.felix.simplebook.view;

/**
 * Created by chaofei.xue on 2018/5/31.
 */

public interface IBackupNetView {
    void setTime(String bTime, String rTime);
    void showMessage(String value);
    void showLoading();
    void closeLoading();
}
