package com.felix.simplebook.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class GetTime {
    public static String get(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String time = formatter.format(curDate);
        return time;
    }
}
