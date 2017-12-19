package com.felix.simplebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.felix.simplebook.database.InfoBean;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/30.
 */

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

    private List<String> simpleList;
    private List<InfoBean> fullList;
    private int flag;
    private final static int SIMPLE = 0;
    private final static int FULL = 1;
    private Context context;

    public ContentAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {;
        return new ViewHolder(null);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {


        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            if (flag == SIMPLE) {

            } else {

            }
        }
    }

    public void simpleInit(List<String> simpleList) {
        this.simpleList = simpleList;
        // init flag simple
        flag = SIMPLE;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
