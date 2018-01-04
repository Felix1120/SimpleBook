package com.felix.simplebook.model;

import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.TypeBean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by chaofei.xue on 2018/1/2.
 */

public class AddModel implements IAddModel {

    @Override
    public void queryData(final ICallBack<List<TypeBean>> callBack) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<TypeBean> lists = DataSupport.findAll(TypeBean.class);
                callBack.successful(lists);
            }
        }.start();
    }
}
