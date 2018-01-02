package com.felix.simplebook.presenter;

import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.model.HomeModel;
import com.felix.simplebook.model.IHomeModel;
import com.felix.simplebook.utils.GetTime;
import com.felix.simplebook.view.IHomeView;

/**
 * Created by chaofei.xue on 2018/1/2.
 */

public class HomePresenter implements IHomePresenter {
    private IHomeModel homeModel;
    private IHomeView homeView;
    private String year, month, day;

    public HomePresenter(IHomeView homeView) {
        this.homeView = homeView;
        homeModel = new HomeModel();
        String time = GetTime.get();
        String[] times = time.split("\\.");
        year = times[0];
        month = times[1];
        day = times[2];
    }

    @Override
    public void query() {
        homeModel.queryData(new ICallBack<String[]>() {
            @Override
            public void successful(String[] strings) {
                homeView.setQueryResult(strings);
            }

            @Override
            public void error(String value) {

            }
        }, year, month, day);
    }
}
