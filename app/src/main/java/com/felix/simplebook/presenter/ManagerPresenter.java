package com.felix.simplebook.presenter;

import com.felix.simplebook.bean.SimpleBean;
import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.fragment.ManagerFragment;
import com.felix.simplebook.model.IManagerModel;
import com.felix.simplebook.model.ManagerModel;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.view.IManagerView;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/28.
 */

public class ManagerPresenter implements IManagerPresenter {
    private IManagerView managerView;
    private IManagerModel managerModel;
    private String year;

    public ManagerPresenter(IManagerView managerView) {
        this.managerView = managerView;
        managerModel = new ManagerModel();
    }

    @Override
    public void queryYear() {
        managerModel.queryDataBase(ManagerModel.YEAR, null, new ICallBack<List<String>>() {
            @Override
            public void successful(List<String> lists) {
                for (String s : lists) {
                    MyLog.info("ManagerPresenter", "年:" + s);
                }
                managerView.update(ManagerFragment.YEAR, lists);
            }

            @Override
            public void error(String value) {
                managerView.showMessage(value);
            }
        });
    }

    @Override
    public void queryMonth(String year) {
        this.year = year;
        managerModel.queryDataBase(ManagerModel.MONTH, year, new ICallBack<List<String>>() {
            @Override
            public void successful(List<String> lists) {
                for (String s : lists) {
                    MyLog.info("ManagerPresenter", "月:" + s);
                }
                managerView.update(ManagerFragment.MONTH, lists);
            }

            @Override
            public void error(String value) {
                managerView.showMessage(value);
            }
        });
    }

    @Override
    public void querySimple(String month) {
        managerModel.querySimple(year, month, new ICallBack<List<SimpleBean>>() {
            @Override
            public void successful(List<SimpleBean> simpleBeans) {
                managerView.updateSimple(simpleBeans);
            }

            @Override
            public void error(String value) {

            }
        });
    }

    @Override
    public void queryFull(String month) {
        managerModel.queryFull(year, month, new ICallBack<List<InfoBean>>() {
            @Override
            public void successful(List<InfoBean> infoBeans) {
                managerView.updateFull(infoBeans);
            }

            @Override
            public void error(String value) {

            }
        });
    }
}
