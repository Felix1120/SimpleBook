package com.felix.simplebook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felix.simplebook.R;
import com.felix.simplebook.bean.SimpleBean;
import com.felix.simplebook.fragment.ManagerFragment;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/30.
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

    private List<SimpleBean> lists;
    private Context context;

    public SimpleAdapter(List<SimpleBean> lists) {
        this.lists = lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.simple_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(lists.get(position).getIn_or_out().equals("in")){
            holder.llSimple.setBackgroundResource(R.color.green);
        }
        holder.tvType.setText(lists.get(position).getType());
        holder.tvMoney.setText(lists.get(position).getMoney());
        holder.tvProportion.setText(lists.get(position).getProportion());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llSimple;
        private TextView tvType;
        private TextView tvMoney;
        private TextView tvProportion;
        public ViewHolder(View itemView) {
            super(itemView);
            llSimple = itemView.findViewById(R.id.ll_simple_item);
            tvType = itemView.findViewById(R.id.tv_type_simple_item);
            tvMoney = itemView.findViewById(R.id.tv_money_simple_item);
            tvProportion = itemView.findViewById(R.id.tv_proportion_simple_item);
        }
    }

    public void updateList(List<SimpleBean> lists) {
        this.lists = lists;
    }
}
