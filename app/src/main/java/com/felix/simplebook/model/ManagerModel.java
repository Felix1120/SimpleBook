package com.felix.simplebook.model;

import android.util.ArraySet;

import com.felix.simplebook.bean.SimpleBean;
import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.database.MyDataBase;
import com.felix.simplebook.database.TypeBean;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.MyTools;

import org.litepal.crud.DataSupport;
import org.litepal.crud.async.FindMultiExecutor;
import org.litepal.crud.callback.FindMultiCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by chaofei.xue on 2017/11/28.
 */

public class ManagerModel implements IManagerModel {
    public static final int YEAR = 0;
    public static final int MONTH = 1;

    private List<SimpleBean> simpleLists;


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
        simpleLists = new ArrayList<>();
        dataBase.query(new Observer<List<InfoBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(final List<InfoBean> infoLists) {

                //获取所有的类型
                List<String> stringList = new ArrayList<>();
                for (InfoBean infoBean : infoLists) {
                    String type = infoBean.getType();
                    if (!stringList.contains(type)) {
                        stringList.add(type);
                    }
                }

                List<TypeBean> typeLists = new ArrayList<>();
                for (String type : stringList) {
                    TypeBean typeBean = new TypeBean();
                    typeBean.setType(type);
                    typeLists.add(typeBean);
                }
                double inMoney = 0;
                double outMoney = 0;

                DecimalFormat dfDouble = new DecimalFormat("0.00");
                DecimalFormat dfInt = new DecimalFormat("0");

                //给simpleLists赋值
                for (TypeBean typeBean : typeLists) {
                    SimpleBean simpleBeanOut = new SimpleBean();
                    simpleBeanOut.setType(typeBean.getType());
                    MyLog.info(typeBean.getType());
                    simpleBeanOut.setMoney("0");
                    simpleBeanOut.setIn_or_out("out");
                    simpleLists.add(simpleBeanOut);
                }

                for (TypeBean typeBean : typeLists) {
                    SimpleBean simpleBeanIn = new SimpleBean();
                    simpleBeanIn.setType(typeBean.getType());
                    simpleBeanIn.setMoney("0");
                    simpleBeanIn.setIn_or_out("in");
                    simpleLists.add(simpleBeanIn);
                }

                for (InfoBean infoBean : infoLists) {
                    //统计总的入和出
                    switch (infoBean.getInOrOut()) {
                        case "in":
                            inMoney += Double.valueOf(infoBean.getMoney());
                            break;
                        case "out":
                            outMoney += Double.valueOf(infoBean.getMoney());
                            break;
                    }
                    for (SimpleBean simpleBean : simpleLists) {
                        //统计各模块的
                        if (simpleBean.getType().equals(infoBean.getType())) {
                            switch (infoBean.getInOrOut()) {
                                case "in":
                                    if (simpleBean.getIn_or_out().equals("in")) {
                                        simpleBean.setMoney(String.valueOf(Double.valueOf
                                                (simpleBean.getMoney()) +
                                                Double.valueOf(infoBean.getMoney())));
                                    }
                                    break;
                                case "out":
                                    if (simpleBean.getIn_or_out().equals("out")) {
                                        simpleBean.setMoney(String.valueOf(Double.valueOf
                                                (simpleBean.getMoney()) +
                                                Double.valueOf(infoBean.getMoney())));
                                    }
                                    break;
                            }
                        }
                    }
                }
                //one for end
                for (SimpleBean simpleBean : simpleLists) {
                    simpleBean.setInMoney(inMoney + "");
                    simpleBean.setOutMoney(outMoney + "");
                    simpleBean.setMoney(dfInt.format(Double.valueOf(simpleBean.getMoney())));
                    switch (simpleBean.getIn_or_out()) {
                        case "in":
                            simpleBean.setProportion(dfDouble.format(Double.valueOf(
                                    simpleBean.getMoney()) / inMoney));
                            break;
                        case "out":
                            simpleBean.setProportion(dfDouble.format(Double.valueOf(
                                    simpleBean.getMoney()) / outMoney));
                            break;
                    }
                }

                //移除money为0的数据
                List<SimpleBean> lists = new ArrayList<>();
                for (int i = 0; i < simpleLists.size(); i++) {
                    if (!simpleLists.get(i).getMoney().equals("0")) {
                        lists.add(simpleLists.get(i));
                    }
                }
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
