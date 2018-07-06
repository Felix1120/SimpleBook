package com.felix.simplebook.view;

import android.content.Context;

import java.io.File;

/**
 * Created by chaofei.xue on 2018/6/4.
 */

public interface IMyCenterView {
    final static int REQUEST_CODE_CHOOSE = 10;
    void initDataView();
    void showNetImg(File file);
    void showLocalUmg();
}
