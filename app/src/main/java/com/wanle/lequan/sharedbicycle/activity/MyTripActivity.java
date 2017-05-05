package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.TripListAdapter;
import com.wanle.lequan.sharedbicycle.bean.TripListBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
import com.wanle.lequan.sharedbicycle.view.EmptyRecyclerView;
import com.wanle.lequan.sharedbicycle.view.RecycleViewDivider;

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

public class MyTripActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.swipe_target)
    EmptyRecyclerView mRecListTrip;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipRefresh;
    private TripListAdapter mMAdapter;
    private View emptyView;
    private TextView mTv_empty;
    private int mPage=1;
    private int endPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip);
        ButterKnife.bind(this);
        initView();
        setRefresh();
    }

    private void setRefresh() {
        mSwipRefresh.setOnRefreshListener(this);
        mSwipRefresh.setOnLoadMoreListener(this);
    }

    private void initView() {
        mTvTitle.setText("我的行程");
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        NetWorkUtil.isNetworkAvailable(this);
        emptyView = findViewById(R.id.empty_view);
        mTv_empty = (TextView) emptyView.findViewById(R.id.tv_empty);
        mTv_empty.setText("暂时没有行程记录哦");
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTripList(true);
            }
        });
        mMAdapter = new TripListAdapter(this);
        mRecListTrip.setAdapter(mMAdapter);
        mRecListTrip.setEmptyView(emptyView);
        netBug();
        mRecListTrip.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (NetWorkUtil.isNetworkAvailable(this)) {
            getTripList(false);
        }
        mRecListTrip.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 3, getResources().getColor(R.color.red)));
    }

    /**
     * 访问网络获取行程列表信息
     */
    private void getTripList(final boolean isRefresh) {
        //String userId="99DBD039C7DCE2DA86889143946687EF6BD790241761D8CD8147EA299742DBCD6B2DC180FD294EC25F7DEBEB1F2B0DA7";
        String userId = getSharedPreferences("userinfo", MODE_PRIVATE).getString("userId", "");
        Log.i("trip1", userId);
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("page", ""+mPage++);
        map.put("rows", "10");
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).getTripList(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {
                        String jsonString = response.body().string();
                        Log.i("trip", jsonString);
                        if (null != jsonString) {
                            Gson gson = new Gson();
                            TripListBean tripListBean = gson.fromJson(jsonString, TripListBean.class);
                            if (tripListBean != null) {
                                if (tripListBean.getResponseCode().equals("1")) {
                                    //Log.i("trip", tripListBean.getResponseObj().size() + "");
                                    endPage=tripListBean.getResponseObj().getPage();
                                    mMAdapter.setData(tripListBean.getResponseObj().getRows(), isRefresh);
                                } else {
                                    ToastUtil.show(MyTripActivity.this, tripListBean.getResponseMsg());
                                }
                            }
                        }
                    }
                    abnormalView();
                } catch (IOException e) {
                    e.printStackTrace();
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
        if (NetWorkUtil.isNetworkAvailable(MyTripActivity.this)) {
            if (mMAdapter.getItemCount() == 0) {
                emptyView.setVisibility(View.VISIBLE);
                mSwipRefresh.setVisibility(View.GONE);
                mTv_empty.setText("您还没有骑行记录哦");
            } else {
                emptyView.setVisibility(View.GONE);
                mSwipRefresh.setVisibility(View.VISIBLE);
            }
        }
    }

    public void netBug() {
        if (!NetWorkUtil.isNetworkAvailable(MyTripActivity.this)) {
            mTv_empty.setText("网络连接失败，连接后点击刷新");
            emptyView.setVisibility(View.VISIBLE);
            mSwipRefresh.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        if (NetWorkUtil.isNetworkAvailable(this)){
            mSwipRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipRefresh.setRefreshing(false);
                    mPage=1;
                    getTripList(true);
                }
            }, 1000);
        }
    }

    @Override
    public void onLoadMore() {
        if (mPage>endPage){
            ToastUtil.show(this,"暂无更多数据");
            mSwipRefresh.setLoadingMore(false);
            return;
        }
        if (NetWorkUtil.isNetworkAvailable(this)){
            mSwipRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipRefresh.setLoadingMore(false);
                    getTripList(false);
                }
            }, 1000);
        }
    }
}
