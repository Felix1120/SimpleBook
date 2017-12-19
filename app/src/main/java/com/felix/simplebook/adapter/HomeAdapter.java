package com.felix.simplebook.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.felix.simplebook.R;
import com.felix.simplebook.activity.HomeDialogActivity;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.utils.MyLog;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HolderView> {
    private List<InfoBean> mLists;
    private Context mContext;


    public HomeAdapter(List<InfoBean> lists) {
        mLists = lists;
    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_item, parent, false);
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(HolderView holder, final int position) {
        MyLog.info("id="  + mLists.get(position).getId());
        holder.tvTime.setVisibility(View.VISIBLE);
        if (position != 0 && mLists.get(position).getTime().equals(mLists.get(position - 1).getTime())) {
            holder.tvTime.setVisibility(View.GONE);
        }
        MyLog.info("HomeAdapter",mLists.get(position).getInOrOut().toString());
        if (mLists.get(position).getInOrOut().equals("in")) {
            holder.mLinearLayout.setBackgroundResource(R.color.green);
        } else {
            holder.mLinearLayout.setBackgroundResource(R.color.red);
        }
        switch (mLists.get(position).getType()){
            case "餐饮":
                holder.imgLeft.setBackgroundResource(R.drawable.coctail);
                holder.imgRight.setBackgroundResource(R.drawable.cheese);
                break;
            case "购物":
                holder.imgLeft.setBackgroundResource(R.drawable.shopping);
                holder.imgRight.setBackgroundResource(R.drawable.shopping_bag);
                break;
            case "工资":
                holder.imgLeft.setBackgroundResource(R.drawable.money);
                holder.imgRight.setBackgroundResource(R.drawable.yen35);
                break;
            case "交通":
                holder.imgLeft.setBackgroundResource(R.drawable.bus);
                holder.imgRight.setBackgroundResource(R.drawable.car);
                break;
            case "其他":
                holder.imgLeft.setBackgroundResource(R.drawable.film_reel);
                holder.imgRight.setBackgroundResource(R.drawable.dumbbell);
                break;
        }

        holder.tvType.setText(mLists.get(position).getType());
        holder.tvTime.setText(mLists.get(position).getTime());
        holder.tvMoney.setText(mLists.get(position).getMoney());
        holder.tvStatus.setText(mLists.get(position).getStatus());

        holder.mLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(mContext, HomeDialogActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("info",mLists.get(position));
                intent.putExtras(mBundle);
                mContext.startActivity(intent);
                MyLog.info("HomeAdapter","被长按" + mLists.get(position).getType());
                return true;
            }
        });

        if(position == mLists.size()-1 && position > 18){
            MyLog.info("最后一条");
            Intent intent = new Intent("com.felix.simplebook.successful");
            intent.putExtra("what", "update");
            mContext.sendBroadcast(intent);
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public static class HolderView extends RecyclerView.ViewHolder {
        private TextView tvMoney;
        private TextView tvTime;
        private TextView tvType;
        private TextView tvStatus;
        private ImageView imgLeft;
        private ImageView imgRight;
        private RelativeLayout mLinearLayout;

        public HolderView(View itemView) {
            super(itemView);
            tvMoney = itemView.findViewById(R.id.tv_money_home_item);
            tvTime = itemView.findViewById(R.id.tv_time_home_item);
            tvType = itemView.findViewById(R.id.tv_type_home_item);
            tvStatus = itemView.findViewById(R.id.tv_status_home_item);
            imgLeft = itemView.findViewById(R.id.img_left_home_item);
            imgRight = itemView.findViewById(R.id.img_right_home_item);
            mLinearLayout = itemView.findViewById(R.id.ll_home_item);
        }
    }
}

