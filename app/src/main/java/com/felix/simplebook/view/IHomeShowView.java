package com.felix.simplebook.view;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public interface IHomeShowView {
    void showMessage(String msg);
    void cancel();
    void setData(String inOrOut, String time, String type, String money, String status);
    void setSpinner(List<String> list);
    void setType(String type);
}
