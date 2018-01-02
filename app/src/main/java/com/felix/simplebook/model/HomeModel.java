package com.felix.simplebook.model;

import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.database.MyDataBase;
import com.felix.simplebook.utils.MyLog;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by chaofei.xue on 2018/1/2.
 */

public class HomeModel implements IHomeModel {
    @Override
    public void queryData(final ICallBack<String[]> callBack, final String year, final String month,
                          final String day) {
        MyDataBase myDataBase = MyDataBase.getInstance();
        myDataBase.query(new Observer<List<InfoBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<InfoBean> infoBeans) {
                int monthInMoney = 0, monthOutMoney = 0;
                int dayInMoney = 0, dayOutMoney = 0;
                String[] result = new String[6];
                for (InfoBean infoBean : infoBeans) {
                    //统计同一个月
                    if (infoBean.getMonth().equals(month) && infoBean.getInOrOut().equals("out")) {
                        monthOutMoney += Integer.valueOf(infoBean.getMoney());
                    } else if (infoBean.getMonth().equals(month) && infoBean.getInOrOut().equals("in")) {
                        monthInMoney += Integer.valueOf(infoBean.getMoney());
                    }
                    //统计同一天
                    if (infoBean.getDay().equals(day) && infoBean.getInOrOut().equals("out")) {
                        dayOutMoney += Integer.valueOf(infoBean.getMoney());
                    } else if (infoBean.getDay().equals(day) && infoBean.getInOrOut().equals("in")) {
                        dayInMoney += Integer.valueOf(infoBean.getMoney());
                    }
                }
                result[0] = month;
                result[1] = day;
                result[2] = monthInMoney + "";
                result[3] = monthOutMoney + "";
                result[4] = dayInMoney + "";
                result[5] = dayOutMoney + "";
                MyLog.info("HomeModel :" + result[0] +"  "+ result[1] +"  " + result[2]
                        +"  "+result[3] +"  " + result[4] +"  "+ result[5]);
                callBack.successful(result);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }, "select * from InfoBean where year = ? and month = ?", year, month);
    }
}
