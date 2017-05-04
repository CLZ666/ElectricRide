package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.LeaseRecordAdapter;
import com.wanle.lequan.sharedbicycle.bean.CdbLeaseRecordBean;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;

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
    private View mEmptyView;
    private TextView mTv_empty;
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
        mEmptyView = findViewById(R.id.empty_view);
        mTv_empty = (TextView) mEmptyView.findViewById(R.id.tv_empty);
        mTv_empty.setText("暂时没有租赁记录哦");
        netBug();
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
        if (NetWorkUtil.isNetworkAvailable(LeaseRecordActivity.this)) {
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
        if (!NetWorkUtil.isNetworkAvailable(LeaseRecordActivity.this)) {
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
