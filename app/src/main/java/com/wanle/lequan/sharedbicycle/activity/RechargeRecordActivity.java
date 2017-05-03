package com.wanle.lequan.sharedbicycle.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.RechargeRecordAdapter;
import com.wanle.lequan.sharedbicycle.bean.RechargeRecordBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;

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

public class RechargeRecordActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tv_titl)
    TextView mTvTitl;
    @BindView(R.id.tv_setting)
    TextView mTvSetting;
    @BindView(R.id.swipe_target)
    ListView mSwipeTarget;
    @BindView(R.id.swip_refresh)
    SwipeToLoadLayout mSwipRefresh;
    private SharedPreferences mMSpUserInfo;
    private String mUserId;
    private RechargeRecordAdapter mAdapter;
    private View mEmptyView;
    private TextView mTv_empty;
    private int mPage = 1;
    private int endPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_record);
        ButterKnife.bind(this);
        NetWorkUtil.isNetworkAvailable(this);
        initView();
        getRecord(false);
    }

    private void initView() {
        mTvTitl.setText("账户明细");
        mTvSetting.setText("退款说明");
        mTvTitl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mTvSetting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        mMSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mUserId = mMSpUserInfo.getString("userId", "");
        mAdapter = new RechargeRecordAdapter(this);
        mEmptyView = findViewById(R.id.empty_view);
        mTv_empty = (TextView) mEmptyView.findViewById(R.id.tv_empty);
        mTv_empty.setText("暂时没有充值记录哦");
        netBug();
        mSwipeTarget.setEmptyView(mEmptyView);
        mSwipeTarget.setAdapter(mAdapter);
        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecord(true);
            }
        });
        mSwipRefresh.setOnRefreshListener(this);
        mSwipRefresh.setOnLoadMoreListener(this);
    }

    private void getRecord(final boolean isRefresh) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", mUserId);
        map.put("page", "" + mPage++);
        map.put("rows", "10");
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).rechargeRecord(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        String jsonString = response.body().string();
                        Log.i("RECORD1", jsonString);
                        if (jsonString != null) {
                            Gson gson = new Gson();
                            RechargeRecordBean dataBean = gson.fromJson(jsonString, RechargeRecordBean.class);
                            if (null != dataBean) {
                                if (dataBean.getResponseCode().equals("1")) {
                                    endPage = dataBean.getResponseObj().getPage();
                                    mAdapter.setData(dataBean.getResponseObj().getRows(), isRefresh);
                                } else {
                                    ToastUtils.getShortToastByString(RechargeRecordActivity.this, dataBean.getResponseMsg());
                                }

                            }
                        }
                        abnormalView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

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
        if (NetWorkUtil.isNetworkAvailable(RechargeRecordActivity.this)) {
            if (mAdapter.getCount() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
                mSwipRefresh.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                mSwipRefresh.setVisibility(View.VISIBLE);
            }
        }
    }

    public void netBug() {
        if (!NetWorkUtil.isNetworkAvailable(RechargeRecordActivity.this)) {
            mTv_empty.setText("网络连接失败，连接后点击刷新");
            mEmptyView.setVisibility(View.VISIBLE);
            mSwipRefresh.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            mSwipRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipRefresh.setRefreshing(false);
                    mPage = 1;
                    getRecord(true);
                }
            }, 1000);
        }
    }

    @Override
    public void onLoadMore() {
        if (mPage > endPage) {
            ToastUtil.show(this, "暂无更多数据");
            mSwipRefresh.setLoadingMore(false);
            return;
        }
        if (NetWorkUtil.isNetworkAvailable(this)) {
            mSwipRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipRefresh.setLoadingMore(false);
                    getRecord(false);
                }
            }, 1000);
        }
    }
}
