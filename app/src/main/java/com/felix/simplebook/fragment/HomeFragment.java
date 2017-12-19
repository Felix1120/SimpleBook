package com.felix.simplebook.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.adapter.HomeAdapter;
import com.felix.simplebook.database.InfoBean;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.MyToast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chaofei.xue on 17-11-10.
 */

public class HomeFragment extends Fragment {

    @BindView(R.id.recycler_view_fragment_home)
    RecyclerView mRecyclerView;

    private Unbinder binder;
    private List<InfoBean> mLists;
    private HomeAdapter mHomeAdapter;

    private int startPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MyLog.info("HomeFragment", "onCreate");
        super.onCreate(savedInstanceState);

        UpdateData update = new UpdateData();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.felix.simplebook.successful");
        getContext().registerReceiver(update, filter);

        startPosition = 20;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyLog.info("HomeFragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        binder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLists = new ArrayList<>();
        mHomeAdapter = new HomeAdapter(mLists);
        mRecyclerView.setAdapter(mHomeAdapter);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(manager);
        updateData();
        getActivity().setTitle(R.string.home);
    }

    @Override
    public void onDestroy() {
        binder.unbind();
        super.onDestroy();
    }

    class UpdateData extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            MyLog.info("HomeFragment", "收到广播 what=" + intent.getStringExtra("what"));
            switch (intent.getStringExtra("what")){
                case "init":
                    updateData();
                    break;
                case "update":
                    updateData(startPosition, 20);
                    break;
            }
        }
    }

    public void updateData(){
        MyLog.info("HomeFragment", "更新数据");
        mLists.clear();
        Observable<List<InfoBean>> mObservable = Observable.create(new ObservableOnSubscribe<List<InfoBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<InfoBean>> emitter) throws Exception {
                //Connector.getDatabase();
                List<InfoBean> lists = DataSupport.limit(20).offset(0).order("time desc").find(InfoBean.class);
                MyLog.info("HomeFragment", "集合长度"+lists.size());
                emitter.onNext(lists);
            }
        });
        Observer<List<InfoBean>> mObserver = new Observer<List<InfoBean>>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(List<InfoBean> lists) {
                if(lists.size() == 0){
                    mHomeAdapter.notifyDataSetChanged();
                }
                for (InfoBean info : lists){
                    mLists.add(info);
                    mHomeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    public void updateData(final int start, final int length){
        MyLog.info("updateData 上拉加载重载方法");
        Observable<List<InfoBean>> mObservable = Observable.create(new ObservableOnSubscribe<List<InfoBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<InfoBean>> emitter) throws Exception {
                //Connector.getDatabase();
                List<InfoBean> lists = DataSupport.limit(length).offset(start).order("time desc").find(InfoBean.class);
                MyLog.info("updateData 上拉加载重载方法", "集合长度"+lists.size());
                emitter.onNext(lists);
            }
        });
        Observer<List<InfoBean>> mObserver = new Observer<List<InfoBean>>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(List<InfoBean> lists) {
                startPosition += lists.size();
                if(lists.size() == 0){
                    MyToast.makeText(getActivity(), "亲，没有更多了", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (InfoBean info : lists){
                    mLists.add(info);
                   // mHomeAdapter.notifyItemInserted(startPosition);
                    mHomeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getActivity().setTitle(R.string.home);
    }
}
