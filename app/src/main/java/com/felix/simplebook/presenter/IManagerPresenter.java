package com.felix.simplebook.presenter;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/25.
 */

public interface IManagerPresenter {
    void queryYear();
    void queryMonth(String year);
    void querySimple(String month);
    void queryFull(String month);
}
