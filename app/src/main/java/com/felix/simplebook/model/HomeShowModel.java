package com.felix.simplebook.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.felix.simplebook.R;
import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.database.TypeBean;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * Created by chaofei.xue on 17-11-20.
 */

public class HomeShowModel implements IHomeShowModel {
    @Override
    public void saveDataRequest(ICallBack<InfoBean> mCallBack, InfoBean mInfoBean, String flag) {
        //写入数据库逻辑
        MyLog.info("HomeShowModel", "saveDataRequest");
        Connector.getDatabase();
        if (flag.equals("save")) {
            boolean save = mInfoBean.save();
            if (save) {
                mCallBack.successful(null);
                MyLog.info("HomeShowModel", "successful");
                MyLog.info("HomeShowModel", "save id:"+mInfoBean.getId());
            } else {
                mCallBack.error("error");
                MyLog.info("HomeShowModel", "error");
            }
        } else if (flag.equals("edit")) {
            InfoBean infoBean = new InfoBean();
            infoBean.setId(mInfoBean.getId());
            infoBean.setInOrOut(mInfoBean.getInOrOut());
            infoBean.setMoney(mInfoBean.getMoney());
            infoBean.setStatus(mInfoBean.getStatus());
            infoBean.setType(mInfoBean.getType());
            infoBean.setTime(mInfoBean.getTime());
            infoBean.setYear(mInfoBean.getYear());
            infoBean.setMonth(mInfoBean.getMonth());
            infoBean.setDay(mInfoBean.getDay());
            int update = infoBean.update(mInfoBean.getId());
            if (update > 0) {
                mCallBack.successful(null);
                MyLog.info("HomeShowModel", "edit successful");
                MyLog.info("HomeShowModel", "edit id:"+mInfoBean.getId());
                MyLog.info("HomeShowModel", "edit update:"+update);
            } else {
                mCallBack.error("error");
                MyLog.info("HomeShowModel", "edit error");
                MyLog.info("HomeShowModel", "id:"+mInfoBean.getId());
            }
        }
    }

    @Override
    public void readType(final ICallBack<List<TypeBean>> mCallBack, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("config.sb",
                Context.MODE_PRIVATE);
        boolean flag = preferences.getBoolean("first_read", false);
        if (!flag) {
            TypeBean typeBean1 = new TypeBean();
            typeBean1.setType(context.getResources().getString(R.string.type_buy));
            typeBean1.save();
            TypeBean typeBean2 = new TypeBean();
            typeBean2.setType(context.getResources().getString(R.string.type_food));
            typeBean2.save();
            TypeBean typeBean3 = new TypeBean();
            typeBean3.setType(context.getResources().getString(R.string.type_money));
            typeBean3.save();
            TypeBean typeBean5 = new TypeBean();
            typeBean5.setType(context.getResources().getString(R.string.type_bus));
            typeBean5.save();
            TypeBean typeBean6 = new TypeBean();
            typeBean6.setType(context.getResources().getString(R.string.type_other));
            typeBean6.save();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("first_read", true).commit();
        }
        List<TypeBean> typeBeans = DataSupport.findAll(TypeBean.class);
        mCallBack.successful(typeBeans);
    }

    @Override
    public void writeType(ICallBack<List<TypeBean>> mCallBack, TypeBean typeBean) {
        boolean save = typeBean.save();
        if (save) {
            mCallBack.successful(null);
        } else {
            mCallBack.error("保存失败");
        }
    }
}
