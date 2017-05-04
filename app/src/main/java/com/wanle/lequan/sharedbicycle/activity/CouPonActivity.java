package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.CopPonRecordAdapter;
import com.wanle.lequan.sharedbicycle.bean.CouPonBean;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CouPonActivity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    private CopPonRecordAdapter mAdapter;
    private List<CouPonBean> mData;
    private View mEmptyView;
    private TextView mTv_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cou_pon);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("优惠券");
        mData = new ArrayList<>();
        mAdapter = new CopPonRecordAdapter(this);
        mEmptyView = findViewById(R.id.empty_view);
        mTv_empty = (TextView) mEmptyView.findViewById(R.id.tv_empty);
        mTv_empty.setText("暂时没有优惠券哦");
        netBug();
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mSwipeTarget.setAdapter(mAdapter);
        CouPonBean data = new CouPonBean("2017-03-08 15:40:15", "2");
        CouPonBean data2 = new CouPonBean("2017-05-0 09:25:10", "5");
        mData.add(data2);
        for (int i = 0; i < 9; i++) {
            mData.add(data);
        }
        mAdapter.setData(mData, false);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abnormalView();
                mAdapter.setData(mData, true);
            }
        });
        abnormalView();
    }

    /**
     * 获取优惠券
     */
    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    public void abnormalView() {
        if (NetWorkUtil.isNetworkAvailable(CouPonActivity.this)) {
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
        if (!NetWorkUtil.isNetworkAvailable(CouPonActivity.this)) {
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
