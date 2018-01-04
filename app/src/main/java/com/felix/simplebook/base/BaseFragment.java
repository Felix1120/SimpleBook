package com.felix.simplebook.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felix.simplebook.utils.MyLog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chaofei.xue on 2017/11/25.
 */

public abstract class BaseFragment extends Fragment {
    public Context mContext;
    public Unbinder bind;
    private String title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initLayout(inflater, container);
        mContext = getContext();
        bind = ButterKnife.bind(this, view);
        title = initTitle();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        getActivity().setTitle(title);
        MyLog.info("BaseFragment", "onActivityCreated");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        initData();
        getActivity().setTitle(title);
        MyLog.info("BaseFragment", "onHiddenChanged");
    }

    //获取布局
    public abstract View initLayout(LayoutInflater inflater, ViewGroup container);

    //初始化控件
    public abstract void initView();

    //初始化数据
    public abstract void initData();

    //设置标题
    public abstract String initTitle();

}
