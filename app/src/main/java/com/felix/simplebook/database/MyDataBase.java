package com.felix.simplebook.database;

import android.database.Cursor;

import com.felix.simplebook.utils.MyLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class MyDataBase {
    private static MyDataBase myDataBase = null;
    public static final int DELETE = 0;
    public static final int ADD = 1;
    public static final int UPDATE = 2;

    private MyDataBase(){
    }
    //单例模式
    public static MyDataBase getInstance(){
        if(myDataBase == null){
            synchronized (MyDataBase.class){
                if(myDataBase == null){
                    myDataBase = new MyDataBase();
                    return myDataBase;
                }
                return myDataBase;
            }
        }
        return myDataBase;
    }

    public void execute(final int what, final InfoBean infoBean, Observer<Boolean> observer){
        Observable<Boolean> observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                switch (what) {
                    case DELETE:
                        int delete = DataSupport.delete(InfoBean.class, infoBean.getId());
                        if (delete > 0) {
                            e.onNext(true);
                        } else {
                            e.onNext(false);
                        }
                        e.onComplete();
                        break;
                    case ADD:
                        boolean save = infoBean.save();
                        e.onNext(save);
                        e.onComplete();
                        break;
                    case UPDATE:
                        InfoBean mInfoBean = new InfoBean();
                        mInfoBean.setId(infoBean.getId());
                        mInfoBean.setInOrOut(infoBean.getInOrOut());
                        mInfoBean.setMoney(infoBean.getMoney());
                        mInfoBean.setStatus(infoBean.getStatus());
                        mInfoBean.setType(infoBean.getType());
                        mInfoBean.setTime(infoBean.getTime());

                        mInfoBean.setYear(infoBean.getYear());
                        mInfoBean.setMonth(infoBean.getMonth());
                        mInfoBean.setDay(infoBean.getDay());
                        int update = mInfoBean.update(infoBean.getId());
                        if (update > 0) {
                            e.onNext(true);
                        } else {
                            e.onNext(false);
                        }
                        e.onComplete();
                        break;
                }
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void query(Observer<List<InfoBean>> observer, final String... where){
        Observable<List<InfoBean>> observable = Observable.create(new ObservableOnSubscribe<List<InfoBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<InfoBean>> e) throws Exception {
                List<InfoBean> lists = new ArrayList<>();
                Cursor cursor = DataSupport.findBySQL(where);

                while (cursor.moveToNext()){
                    long id = cursor.getLong(cursor.getColumnIndex("id"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String money = cursor.getString(cursor.getColumnIndex("money"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
                    String status = cursor.getString(cursor.getColumnIndex("status"));
                    String inOrOut = cursor.getString(cursor.getColumnIndex("inorout"));
                    String year = cursor.getString(cursor.getColumnIndex("year"));
                    String month = cursor.getString(cursor.getColumnIndex("month"));
                    String day = cursor.getString(cursor.getColumnIndex("day"));
                    MyLog.info(id+"  "+time+"  "+money+"  "+type+"  "+status+"  "+inOrOut+"  "
                            +year+"  "+month+"  "+day);
                    InfoBean infoBean = new InfoBean(id, year, month, day, time, type, money, status, inOrOut);
                    lists.add(infoBean);
                }
                e.onNext(lists);
                e.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
