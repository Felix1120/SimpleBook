package com.felix.simplebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felix.simplebook.R;
import com.felix.simplebook.callback.IOnClickListener;
import com.felix.simplebook.utils.MyLog;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/29.
 */

public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.ViewHolder> {
    private List<String> lists;
    private Context context;
    private IOnClickListener listener;
    public SpinnerAdapter(List<String> lists, IOnClickListener listener){
        this.lists = lists;
        this.listener = listener;
        MyLog.info("弹窗：SpinnerAdapter");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvItem.setText(lists.get(position));
        MyLog.info("弹窗：SpinnerAdapter-->"+lists.get(position));
        holder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(lists.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem;
        public ViewHolder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_spinner_item);
        }
    }
}
