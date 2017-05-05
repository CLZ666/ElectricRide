package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.CopPonRecordAdapter;
import com.wanle.lequan.sharedbicycle.bean.CouPonBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouPonActivity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    private CopPonRecordAdapter mAdapter;
    private View mEmptyView;
    private TextView mTv_empty;
    private int mPage = 1;
    private int endPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cou_pon);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("优惠券");
        mAdapter = new CopPonRecordAdapter(this);
        mEmptyView = findViewById(R.id.empty_view);
        mTv_empty = (TextView) mEmptyView.findViewById(R.id.tv_empty);
        mTv_empty.setText("您还没有优惠券哦");
        netBug();
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mSwipeTarget.setAdapter(mAdapter);

        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCouPon(false);
            }
        });
    }

    /**
     * 获取优惠券
     */
    public void getCouPon(final boolean isRefrsh) {
        String userId = getSharedPreferences("userinfo", MODE_PRIVATE).getString("userId", "");
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("page", "" + mPage++);
        map.put("rows", "10");
        final Call<ResponseBody> call = HttpUtil.getService(ApiService.class).mycoupon(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        final String jsonString = response.body().string();
                        Log.i("coupon1",jsonString);
                        if (null != jsonString) {
                            Gson gson = new Gson();
                            final CouPonBean couPonBean = gson.fromJson(jsonString, CouPonBean.class);
                            if (null != couPonBean) {
                                if (couPonBean.getResponseCode().equals("1")) {
                                    mAdapter.setData(couPonBean.getResponseObj().getRows(), isRefrsh);
                                    endPage = couPonBean.getResponseObj().getPage();
                                } else {
                                    ToastUtil.show(CouPonActivity.this, couPonBean.getResponseMsg());
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                abnormalView();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    public void abnormalView() {
        if (NetWorkUtil.isNetworkAvailable(CouPonActivity.this)) {
            if (mAdapter.getItemCount() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
                mSwipeToLoadLayout.setVisibility(View.GONE);
                mTv_empty.setText("您还没有优惠券哦");
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
        } else {
            getCouPon(false);
        }
    }

    @Override
    public void onRefresh() {
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(false);
                mPage = 1;
                getCouPon(true);
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        if (mPage > endPage) {
            ToastUtil.show(this, "暂无更多数据");
            mSwipeToLoadLayout.setLoadingMore(false);
            return;
        }
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setLoadingMore(false);
                getCouPon(false);
            }
        }, 1000);
    }
}
