package com.felix.simplebook.model;

import com.felix.simplebook.callback.ICallBack;

/**
 * Created by Felix on 2018/6/5.
 */

public interface IMyCenterModel {
    void updateImg(ICallBack<String> callBack, String imagePath);
}
