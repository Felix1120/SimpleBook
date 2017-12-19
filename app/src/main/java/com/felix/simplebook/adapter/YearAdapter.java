package com.felix.simplebook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felix.simplebook.R;
import com.felix.simplebook.fragment.ManagerFragment;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/25.
 */

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.HolderView> {
    private List<String> lists;
    private Context mContext;
    private TextView currentTv;
    private ManagerFragment fragment;

    public YearAdapter(List<String> lists, ManagerFragment fragment) {
        this.lists = lists;
        this.fragment = fragment;
    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.year_item, parent, false);
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(final HolderView holder, final int position) {
        holder.tvYear.setText(lists.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateColor(holder.tvYear);
                fragment.queryMonth(lists.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class HolderView extends RecyclerView.ViewHolder {
        public TextView tvYear;
        public CardView linearLayout;

        public HolderView(View itemView) {
            super(itemView);
            tvYear = itemView.findViewById(R.id.tv_year_item);
            linearLayout = itemView.findViewById(R.id.ll_year_item);
        }
    }

    public void updateList(List<String> lists) {
        this.lists = lists;
    }

    public void updateColor(TextView mTv) {
        if (currentTv == null) {
            mTv.setBackgroundResource(R.color.red);
        } else {
            currentTv.setBackgroundResource(R.color.green);
            mTv.setBackgroundResource(R.color.red);
        }
        currentTv = mTv;
    }
}
