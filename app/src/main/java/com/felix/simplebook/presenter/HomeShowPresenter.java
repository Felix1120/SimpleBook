package com.felix.simplebook.presenter;


import android.content.Context;
import android.content.Intent;

import com.felix.simplebook.R;
import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.model.HomeShowModel;
import com.felix.simplebook.model.IHomeShowModel;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.database.TypeBean;
import com.felix.simplebook.view.IHomeShowView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class HomeShowPresenter implements IHomeShowPresenter {
    private IHomeShowView mShowView;
    private IHomeShowModel mShowModel;
    private Context mContext;
    private String mFlag;
    private InfoBean info;

    public HomeShowPresenter(IHomeShowView showView, Context mContext) {
        mShowView = showView;
        this.mContext = mContext;
        mShowModel = new HomeShowModel();
    }

    @Override
    public void cancel() {

    }

    @Override
    public void saveData(String inOrOut, String time, String type,
                         String money, String status) {
        if (inOrOut.equals("") || time.equals("") || type.equals("") ||
                money.equals("")) {
            mShowView.showMessage("不能为空");
            return;
        }
        if (status.equals("")){
            status = type;
        }
        if(money.split("\\.")[0].length() > 6){
            mShowView.showMessage("亲，金额太多了，我数不过来");
            return;
        }
        if(money.split("\\.").length > 1 && money.split("\\.")[1].length() > 2){
            mShowView.showMessage("亲，只能到小数点后两位");
            return;
        }
        if (!checkTime(time)) {
            mShowView.showMessage("时间格式不正确");
            return;
        }
        String[] values = time.split("\\.");
        MyLog.info("数组长度："+values.length);
        if (mFlag.equals("save")) {
            info = new InfoBean(values[0], values[1], values[2], time, type, money, status, inOrOut);
            MyLog.info("HomeShowPresenter", "save");
        } else if (mFlag.equals("edit")) {
            info = new InfoBean(info.getId(), values[0], values[1], values[2], time, type, money, status, inOrOut);
            MyLog.info("HomeShowPresenter", "edit");
        }
        mShowModel.saveDataRequest(new ICallBack<InfoBean>() {
            @Override
            public void successful(InfoBean bean) {
                mShowView.showMessage(mContext.getResources().getString(R.string.success));
                MyLog.info("HomeShowPresenter", "successful");
                mShowView.cancel();
                Intent intent = new Intent("com.felix.simplebook.successful");
                intent.putExtra("what", "init");
                mContext.sendBroadcast(intent);
            }

            @Override
            public void error(String value) {
                mShowView.showMessage(mContext.getResources().getString(R.string.error));
                MyLog.info("HomeShowPresenter", "error");
            }
        }, info, mFlag);
    }

    @Override
    public void setIntent(Intent intent) {
        mFlag = "save";
        String flag = intent.getStringExtra("flag");
        if (flag != null && flag.equals("HomeDialogActivity")) {
            info = (InfoBean) intent.getExtras().get("info");
            mShowView.setData(info.getInOrOut(), info.getTime(), info.getType(),
                    info.getMoney(), info.getStatus());
            mFlag = "edit";
        }
    }

    public boolean checkTime(String time) {
        String[] values = time.split("\\.");
        MyLog.info("时间格式判断--->原值：", time);
        MyLog.info("时间格式判断--->长度：", values.length + "");
        if (values.length == 3) {
            if (values[0].length() == 4 && values[1].length() == 2
                    && values[2].length() == 2) {
                int year = Integer.valueOf(values[0]);
                int month = Integer.valueOf(values[1]);
                int day = Integer.valueOf(values[2]);
                if (year > 2000 && year < 3000) {
                    switch (month) {
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                        case 12:
                            if (day > 0 && day < 32) {
                                return true;
                            } else {
                                return false;
                            }
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            if (day > 0 && day < 31) {
                                return true;
                            } else {
                                return false;
                            }
                        case 2:
                            if (day > 0 && day < 30) {
                                return true;
                            } else {
                                return false;
                            }
                        default:
                            return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    @Override
    public void getList() {
        mShowModel.readType(new ICallBack<List<TypeBean>>() {
            @Override
            public void successful(List<TypeBean> typeBeans) {
                List<String> list = new ArrayList<>();
                for(TypeBean typeBean : typeBeans){
                    list.add(typeBean.getType());
                }
                mShowView.setSpinner(list);
            }
            @Override
            public void error(String value) {

            }
        }, mContext);
    }
}
