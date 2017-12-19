package com.felix.simplebook.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.felix.simplebook.R;
import com.felix.simplebook.adapter.SpinnerAdapter;
import com.felix.simplebook.callback.IOnClickListener;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/29.
 */

public class SpinnerPopWindow extends PopupWindow {

    private List<String> lists;
    private Context context;
    private RecyclerView recyclerView;

    public SpinnerPopWindow(Context context, List<String> lists, IOnClickListener listener) {
        MyLog.info("弹窗初始化");
        this.lists = lists;
        this.context = context;
        init(listener);
    }

    private void init(IOnClickListener listener){
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_window_layou, null);
        setContentView(view);
        setWidth(RecyclerView.LayoutParams.WRAP_CONTENT);
        setHeight(RecyclerView.LayoutParams.WRAP_CONTENT);
        update();
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.transparent_black_3));
        setBackgroundDrawable(dw);

        SpinnerAdapter adapter = new SpinnerAdapter(lists, listener);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView = view.findViewById(R.id.rv_spinner_window_layout);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }
}
