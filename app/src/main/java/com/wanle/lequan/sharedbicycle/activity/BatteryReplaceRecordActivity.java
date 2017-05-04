package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.BatteryReplaceRecordAdapter;
import com.wanle.lequan.sharedbicycle.bean.BatteryReplaceBean;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.view.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BatteryReplaceRecordActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.swipe_target)
    EmptyRecyclerView mSwipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    private BatteryReplaceRecordAdapter mAdapter;
    private List<BatteryReplaceBean> mdata;
    private View mEmptyView;
    private TextView mTv_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_replace_record);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("电池更换记录");
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new BatteryReplaceRecordAdapter(this);
        mEmptyView = findViewById(R.id.empty_view);
        mTv_empty = (TextView) mEmptyView.findViewById(R.id.tv_empty);
        mTv_empty.setText("暂时没有电池更换记录哦");
        netBug();
        mdata = new ArrayList<>();
        BatteryReplaceBean data = new BatteryReplaceBean(1, "编号:51765818619", "2", "2017-04-01 15:30:14");
        BatteryReplaceBean data2 = new BatteryReplaceBean(0, "编号:65717516365", "3", "2017-04-15 16:35:20");
        mdata.add(data);
        for (int i = 0; i < 9; i++) {
            mdata.add(data2);
        }
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setData(mdata, false);
        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abnormalView();
                mAdapter.setData(mdata,true);
            }
        });
        abnormalView();
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    public void abnormalView() {
       if (NetWorkUtil.isNetworkAvailable(BatteryReplaceRecordActivity.this)) {
            if (mAdapter.getItemCount() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
                mSwipeToLoadLayout.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                mSwipeToLoadLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    public void netBug() {
        if (!NetWorkUtil.isNetworkAvailable(BatteryReplaceRecordActivity.this)) {
            mTv_empty.setText("网络连接失败，连接后点击刷新");
            mEmptyView.setVisibility(View.VISIBLE);
            mSwipeToLoadLayout.setVisibility(View.GONE);
        }
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
