package com.felix.simplebook.presenter;

/**
 * Created by chaofei.xue on 2018/6/8.
 */

public interface IRegisterPresenter {
    void register(String username, String password, String rePassword,
                  String email, String phone, String photos);
}
