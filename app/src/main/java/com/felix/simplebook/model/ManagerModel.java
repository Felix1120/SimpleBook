package com.felix.simplebook.model;

import com.felix.simplebook.bean.SimpleBean;
import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.database.MyDataBase;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.MyTools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by chaofei.xue on 2017/11/28.
 */

public class ManagerModel implements IManagerModel {
    public static final int YEAR = 0;
    public static final int MONTH = 1;

    @Override
    public void queryDataBase(int flag, String year, final ICallBack<List<String>> callBack) {
        MyDataBase dataBase = MyDataBase.getInstance();
        switch (flag) {
            case YEAR:
                dataBase.query(new Observer<List<InfoBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<InfoBean> infoBeans) {
                        List<String> lists = new ArrayList<>();
                        for (InfoBean info : infoBeans) {
                            String year = info.getYear();
                            lists.add(year);
                            MyLog.info("ManagerModel", "查询YEAR结果：" + year);
                        }
                        if (lists.size() == 0) {
                            callBack.error("亲，暂时还没有数据哟");
                        } else {
                            List<String> mList = MyTools.singleList(lists);
                            callBack.successful(mList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }, "select * from InfoBean order by year");
                break;

            case MONTH:
                dataBase.query(new Observer<List<InfoBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<InfoBean> infoBeans) {
                        List<String> lists = new ArrayList<>();
                        for (InfoBean info : infoBeans) {
                            String month = info.getMonth();
                            MyLog.info("ManagerModel", "查询MONTH结果：" + month);
                            lists.add(month);
                        }
                        if (lists.size() == 0) {
                            callBack.error("亲，暂时还没有数据哟");
                        } else {
                            List<String> mList = MyTools.singleList(lists);
                            callBack.successful(mList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }, "select * from InfoBean where year = ? order by month", year);
                break;
        }
    }

    @Override
    public void querySimple(String year, String month, final ICallBack<List<SimpleBean>> callBack) {
        MyDataBase dataBase = MyDataBase.getInstance();
        dataBase.query(new Observer<List<InfoBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<InfoBean> infoBeans) {
                double inMoney = 0;
                double outMoney = 0;
                double moneyBuy = 0;
                double moneyFood = 0;
                double moneyBus = 0;
                double moneyOther = 0;
                double moneys = 0;

                List<SimpleBean> lists = new ArrayList<>();
                for (InfoBean infoBean : infoBeans) {
                    switch (infoBean.getType()) {
                        case "购物":
                            double money1 = Double.valueOf(infoBean.getMoney());
                            moneyBuy += money1;
                            if (infoBean.getInOrOut().equals("in")) {
                                inMoney += money1;
                            } else {
                                outMoney += money1;
                            }
                            break;
                        case "餐饮":
                            double money2 = Double.valueOf(infoBean.getMoney());
                            moneyFood += money2;
                            if (infoBean.getInOrOut().equals("in")) {
                                inMoney += money2;
                            } else {
                                outMoney += money2;
                            }
                            break;
                        case "工资":
                            double money3 = Double.valueOf(infoBean.getMoney());
                            moneys += money3;
                            if (infoBean.getInOrOut().equals("in")) {
                                inMoney += money3;
                            } else {
                                outMoney += money3;
                            }
                            break;
                        case "交通":
                            double money4 = Double.valueOf(infoBean.getMoney());
                            moneyBus += money4;
                            if (infoBean.getInOrOut().equals("in")) {
                                inMoney += money4;
                            } else {
                                outMoney += money4;
                            }
                            break;
                        case "其他":
                            double money5 = Double.valueOf(infoBean.getMoney());
                            moneyOther += money5;
                            if (infoBean.getInOrOut().equals("in")) {
                                inMoney += money5;
                            } else {
                                outMoney += money5;
                            }
                            break;
                    }
                }
                DecimalFormat df = new DecimalFormat("0");
                DecimalFormat dfInt = new DecimalFormat("0");
                String[] types = {"购物", "餐饮", "交通", "其他"};
                MyLog.info("ManagerModel", "outMoney=" + outMoney + "  购物=" + moneyBuy);
                String[] proportions = {df.format(moneyBuy / outMoney),
                        df.format(moneyFood / outMoney),
                        df.format(moneyBus / outMoney),
                        df.format(moneyOther / outMoney)};
                double[] doubles = {moneyBuy, moneyFood, moneyBus, moneyOther};
                for (int i = 0; i < types.length; i++) {
                    SimpleBean simpleBean = new SimpleBean(inMoney + "", outMoney + "", types[i]
                            , dfInt.format(doubles[i]) + "", "0", proportions[i], "out");
                    lists.add(simpleBean);
                }

                SimpleBean simpleBean = new SimpleBean(inMoney + "", outMoney + "", "工资"
                        , dfInt.format(moneys) + "", "0", df.format(moneys / inMoney), "in");
                lists.add(simpleBean);

                callBack.successful(lists);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }, "select * from InfoBean where year = ? and month = ? order by month", year, month);
    }

    @Override
    public void queryFull(String year, String month, final ICallBack<List<InfoBean>> callBack) {
        MyDataBase db = MyDataBase.getInstance();
        db.query(new Observer<List<InfoBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<InfoBean> infoBeans) {
                callBack.successful(infoBeans);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }, "select * from InfoBean where year = ? and month = ? order by day", year, month);
    }
}
