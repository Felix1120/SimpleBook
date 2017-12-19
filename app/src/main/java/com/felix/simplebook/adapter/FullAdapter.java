package com.felix.simplebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felix.simplebook.R;
import com.felix.simplebook.bean.SimpleBean;
import com.felix.simplebook.database.InfoBean;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/30.
 */

public class FullAdapter extends RecyclerView.Adapter<FullAdapter.ViewHolder> {

    private List<InfoBean> lists;
    private Context context;

    public FullAdapter(List<InfoBean> lists) {
        this.lists = lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.full_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.llFull.setBackgroundResource(R.color.red);
        if(lists.get(position).getInOrOut().equals("in")){
            holder.llFull.setBackgroundResource(R.color.green);
        }

        holder.tvMoney.setText(lists.get(position).getMoney());
        holder.tvStatus.setText(lists.get(position).getStatus());
        holder.tvType.setText(lists.get(position).getType());
        holder.tvTime.setText(lists.get(position).getDay());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llFull;
        private TextView tvType;
        private TextView tvMoney;
        private TextView tvStatus;
        private TextView tvTime;
        public ViewHolder(View itemView) {
            super(itemView);
            llFull = itemView.findViewById(R.id.ll_full_item);
            tvType = itemView.findViewById(R.id.tv_type_full_item);
            tvMoney = itemView.findViewById(R.id.tv_money_full_item);
            tvStatus = itemView.findViewById(R.id.tv_status__full_item);
            tvTime = itemView.findViewById(R.id.tv_time__full_item);
        }
    }

    public void updateList(List<InfoBean> lists) {
        this.lists = lists;
    }
}
