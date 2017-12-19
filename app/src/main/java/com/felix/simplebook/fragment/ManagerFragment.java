package com.felix.simplebook.fragment;


import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.adapter.FullAdapter;
import com.felix.simplebook.adapter.MonthAdapter;
import com.felix.simplebook.adapter.SimpleAdapter;
import com.felix.simplebook.adapter.YearAdapter;
import com.felix.simplebook.base.BaseFragment;
import com.felix.simplebook.bean.SimpleBean;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.presenter.ManagerPresenter;
import com.felix.simplebook.utils.MyToast;
import com.felix.simplebook.view.IManagerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by chaofei.xue on 2017/11/25.
 */

public class ManagerFragment extends BaseFragment implements IManagerView {

    @BindView(R.id.recycler_year_fragment_manager)
    RecyclerView yearRv;
    @BindView(R.id.recycler_month_fragment_manager)
    RecyclerView monthRv;
    @BindView(R.id.recycler_content_fragment_manager)
    RecyclerView contentRv;
    @BindView(R.id.rv_simple_fragment_manager)
    RecyclerView simpleRv;
    @BindView(R.id.tv_total_in_simple_manager_item)
    TextView tvIn;
    @BindView(R.id.tv_total_out_simple_manager_item)
    TextView tvOut;
    @BindView(R.id.tv_save_simple_manager_item)
    TextView tvSave;
    @BindView(R.id.cv_fragment_manager)
    CardView cardView;

    private YearAdapter yearAdapter;
    private MonthAdapter monthAdapter;
    private SimpleAdapter simpleAdapter;
    private FullAdapter fullAdapter;

    private List<String> listsYear;
    private List<String> listsMonth;
    private List<SimpleBean> listsSimple;
    private List<InfoBean> listsFull;

    private ManagerPresenter presenter;


    public static final int YEAR = 0;
    public static final int MONTH = 1;

    @Override
    public View initLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_manager, container, false);
    }

    @Override
    public void initView() {
        //set year adapter
        listsYear = new ArrayList<>();
        yearAdapter = new YearAdapter(listsYear, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        yearRv.setAdapter(yearAdapter);
        yearRv.setLayoutManager(layoutManager);

        //set month adapter
        listsMonth = new ArrayList<>();
        monthAdapter = new MonthAdapter(listsMonth, this);
        monthRv.setAdapter(monthAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        monthRv.setLayoutManager(manager);

        //set simple adapter
        listsSimple = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(listsSimple);
        simpleRv.setAdapter(simpleAdapter);
        LinearLayoutManager simpleManager = new LinearLayoutManager(mContext);
        simpleRv.setLayoutManager(simpleManager);

        //set full adapter
        listsFull = new ArrayList<>();
        fullAdapter = new FullAdapter(listsFull);
        contentRv.setAdapter(fullAdapter);
        LinearLayoutManager fullManager = new LinearLayoutManager(mContext);
        contentRv.setLayoutManager(fullManager);

        cardView.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        presenter = new ManagerPresenter(this);
        presenter.queryYear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
    //update data
    @Override
    public void update(int flag, List<String> lists) {
        switch (flag){
            case YEAR:
                this.listsYear = null;
                yearAdapter.updateList(lists);
                yearAdapter.notifyDataSetChanged();
                break;
            case MONTH:
                this.listsMonth = null;
                monthAdapter.updateList(lists);
                monthAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void showMessage(String msg) {
        MyToast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void queryMonth(String year){
        presenter.queryMonth(year);
    }

    @Override
    public void updateSimple(List<SimpleBean> lists) {
        cardView.setVisibility(View.VISIBLE);
        DecimalFormat dfInt = new DecimalFormat("0");
        this.listsSimple = null;
        simpleAdapter.updateList(lists);
        simpleAdapter.notifyDataSetChanged();
        tvIn.setText(lists.get(0).getInMoney().split("\\.")[0]);
        tvOut.setText(lists.get(0).getOutMoney().split("\\.")[0]);
        tvSave.setText(dfInt.format(Double.valueOf(lists.get(0).getInMoney()) -
                Double.valueOf(lists.get(0).getOutMoney())) + "");
    }
    public void querySimple(String month){
        presenter.querySimple(month);
    }

    @Override
    public void updateFull(List<InfoBean> lists) {
        this.listsFull = null;
        fullAdapter.updateList(lists);
        fullAdapter.notifyDataSetChanged();
    }
    public void queryFull(String month){
        presenter.queryFull(month);
    }

    @Override
    public String initTitle() {
        return mContext.getResources().getString(R.string.manager);
    }
}
