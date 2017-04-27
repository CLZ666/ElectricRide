package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.IntegralRecordAdapter;
import com.wanle.lequan.sharedbicycle.bean.IntegralRecordBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntegralRecordActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    private IntegralRecordAdapter mAdapter;
    private List<IntegralRecordBean> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_record);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("积分记录");
        mData=new ArrayList<>();
        mAdapter=new IntegralRecordAdapter(this);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mSwipeTarget.setAdapter(mAdapter);
        IntegralRecordBean data=new IntegralRecordBean("骑行结束","2017-03-18 14:15:46","+12");
        IntegralRecordBean data2=new IntegralRecordBean("反馈车辆问题","2017-02-15 09:29:13","+12");
        mData.add(data2);
        for (int i=0;i<9;i++){
            mData.add(data);
        }
        mAdapter.setData(mData,false);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onRefresh() {
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(false);
                mAdapter.setData(mData, true);
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setLoadingMore(false);
                mAdapter.setData(mData, false);
            }
        }, 1000);
    }
}
