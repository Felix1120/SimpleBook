package com.felix.simplebook.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.felix.simplebook.R;
import com.felix.simplebook.activity.AddDialogActivity;
import com.felix.simplebook.adapter.AddAdapter;
import com.felix.simplebook.base.BaseFragment;
import com.felix.simplebook.database.TypeBean;
import com.felix.simplebook.presenter.AddPresenter;
import com.felix.simplebook.presenter.IAddPresenter;
import com.felix.simplebook.view.IAddView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by chaofei.xue on 2018/1/4.
 */

public class AddFragment extends BaseFragment implements IAddView {
    @BindView(R.id.recycler_view_fragment_add)
    RecyclerView mRecyclerView;
    @BindView(R.id.rl_add_fragment_add)
    RelativeLayout rlBody;

    private AddAdapter mAddAdapter;
    private List<TypeBean> mLists;
    private IAddPresenter addPresenter;
    public final static String UPDATE_ADD_FRAGMENT = "com.felix.simplebook.add.update";

    @Override
    public View initLayout(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        return view;
    }

    @Override
    public void initView() {
        mLists = new ArrayList<>();
        addPresenter = new AddPresenter(AddFragment.this);
        mAddAdapter = new AddAdapter(mLists);

        //注册广播
        MyReceiver receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_ADD_FRAGMENT);
        getContext().registerReceiver(receiver, filter);

    }

    @Override
    public void initData() {
        addPresenter.query();
        mRecyclerView.setAdapter(mAddAdapter);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(manager);

        rlBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddDialogActivity.class));
            }
        });
    }

    @Override
    public String initTitle() {
        return "添加类型";
    }

    @Override
    public void show(List<TypeBean> lists) {
        mLists = null;
        mAddAdapter.updateLists(lists);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mAddAdapter.notifyDataSetChanged();
            }
        });
    }

    private class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            addPresenter.query();
        }
    }
}
