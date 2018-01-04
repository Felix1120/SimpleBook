package com.felix.simplebook.presenter;

import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.TypeBean;
import com.felix.simplebook.model.AddModel;
import com.felix.simplebook.model.IAddModel;
import com.felix.simplebook.view.IAddView;

import java.util.List;

/**
 * Created by chaofei.xue on 2018/1/2.
 */

public class AddPresenter implements IAddPresenter {
    private IAddView mAddView;
    private IAddModel mAddModel;

    public AddPresenter(IAddView addView) {
        mAddView = addView;
        mAddModel = new AddModel();
    }

    @Override
    public void query() {
        mAddModel.queryData(new ICallBack<List<TypeBean>>() {
            @Override
            public void successful(List<TypeBean> lists) {
                mAddView.show(lists);
            }

            @Override
            public void error(String value) {

            }
        });
    }
}
