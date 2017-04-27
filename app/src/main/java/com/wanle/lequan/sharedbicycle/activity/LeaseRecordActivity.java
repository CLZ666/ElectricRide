package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.LeaseRecordAdapter;
import com.wanle.lequan.sharedbicycle.bean.CdbLeaseRecordBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeaseRecordActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;


    private LeaseRecordAdapter mAdapter;
    private List<CdbLeaseRecordBean> mdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease_record);
        ButterKnife.bind(this);
        initView();
        //  autoRefresh();
    }

    private void initView() {
        mTvTitle.setText("租赁记录");
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new LeaseRecordAdapter(this);
        mdata = new ArrayList<>();
        CdbLeaseRecordBean data = new CdbLeaseRecordBean(1, 81519966, "3", "2");
        CdbLeaseRecordBean data2 = new CdbLeaseRecordBean(0, 7289762, "4", "6");
        mdata.add(data);
        for (int i = 0; i < 9; i++) {
            mdata.add(data2);
        }
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setData(mdata, false);
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
                mAdapter.setData(mdata, true);
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setLoadingMore(false);
                mAdapter.setData(mdata, false);
            }
        }, 1000);
    }

}
