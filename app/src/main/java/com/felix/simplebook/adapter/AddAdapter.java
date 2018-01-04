package com.felix.simplebook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.activity.AddDialogActivity;
import com.felix.simplebook.database.TypeBean;
import com.felix.simplebook.fragment.AddFragment;
import com.felix.simplebook.utils.MyToast;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by chaofei.xue on 2017/11/30.
 */

public class AddAdapter extends RecyclerView.Adapter<AddAdapter.ViewHolder> {

    private Context context;
    private List<TypeBean> lists;

    public AddAdapter(List<TypeBean> lists) {
        this.lists = lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.add_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTitle.setText(lists.get(position).getType());
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddDialogActivity.class);
                intent.putExtra("info", lists.get(position));
                context.startActivity(intent);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int delete = DataSupport.delete(TypeBean.class, lists.get(position).getId());
                if (delete > 0) {
                    Intent intent = new Intent();
                    intent.setAction(AddFragment.UPDATE_ADD_FRAGMENT);
                    context.sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView imgDelete;
        private ImageView imgEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_add_item);
            imgDelete = itemView.findViewById(R.id.img_delete_add_item);
            imgEdit = itemView.findViewById(R.id.img_edit_add_item);
        }
    }

    public void updateLists(List<TypeBean> lists) {
        this.lists = lists;
    }

}
