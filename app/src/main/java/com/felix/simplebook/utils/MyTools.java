package com.felix.simplebook.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/28.
 */

public class MyTools {
    public static List<String> singleList(List<String> list) {
        List<String> lists = new ArrayList<>();
        for (String str : list) {
            boolean flag = true;
            for (String s : lists) {
                if (str.equals(s)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                lists.add(str);
            }
        }
        return lists;
    }
}
