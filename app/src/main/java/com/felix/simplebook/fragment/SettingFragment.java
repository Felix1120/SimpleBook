package com.felix.simplebook.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseFragment;

/**
 * Created by XCF on 2017/12/18.
 */

public class SettingFragment extends BaseFragment {
    @Override
    public View initLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public String initTitle() {
        return null;
    }
}
