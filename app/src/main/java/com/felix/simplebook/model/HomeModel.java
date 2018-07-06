package com.felix.simplebook.model;

import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.database.MyDataBase;
import com.felix.simplebook.utils.MyLog;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
                double monthInMoney = 0, monthOutMoney = 0;
                double dayInMoney = 0, dayOutMoney = 0;
                DecimalFormat dfInt = new DecimalFormat("0");
                String[] result = new String[6];
                for (InfoBean infoBean : infoBeans) {
                    //统计同一个月
                    if (infoBean.getMonth().equals(month) && infoBean.getInOrOut().equals("out")) {
                        monthOutMoney += Double.valueOf(infoBean.getMoney());
                    } else if (infoBean.getMonth().equals(month) && infoBean.getInOrOut().equals("in")) {
                        monthInMoney += Double.valueOf(infoBean.getMoney());
                    }
                    //统计同一天
                    if (infoBean.getDay().equals(day) && infoBean.getInOrOut().equals("out")) {
                        dayOutMoney += Double.valueOf(infoBean.getMoney());
                    } else if (infoBean.getDay().equals(day) && infoBean.getInOrOut().equals("in")) {
                        dayInMoney += Double.valueOf(infoBean.getMoney());
                    }
                }
                result[0] = month;
                result[1] = day;
                result[2] = dfInt.format(monthInMoney);
                result[3] = dfInt.format(monthOutMoney);
                result[4] = dfInt.format(dayInMoney);
                result[5] = dfInt.format(dayOutMoney);
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

    @Override
    public void checkApkVersion(final Observer<String> observer) {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkHttpClient client = new OkHttpClient();

                final Request request = new Request.Builder()
                        .url("http://47.106.219.34:8080/jz_server/CheckVersion")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        e.onNext(response.body().string());
                        e.onComplete();
                    }
                });
            }
        });

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
