package com.felix.simplebook.view;

import com.felix.simplebook.bean.SimpleBean;
import com.felix.simplebook.database.InfoBean;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/28.
 */

public interface IManagerView {
    void update(int flag, List<String> lists);
    void showMessage(String msg);
    void updateSimple(List<SimpleBean> lists);
    void updateFull(List<InfoBean> lists);
}
