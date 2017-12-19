package com.felix.simplebook.presenter;

import android.content.Intent;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public interface IHomeShowPresenter {
    void cancel();
    void saveData(String inOrOut, String time, String type, String money, String status);
    void setIntent(Intent intent);
    void getList();
}
