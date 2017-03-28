package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

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

public class MyTripActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rec_list_trip)
    EmptyRecyclerView mRecListTrip;
    @BindView(R.id.swip_refresh)
    SwipeRefreshLayout mSwipRefresh;
    private TripListAdapter mMAdapter;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip);
        ButterKnife.bind(this);
        initView();
        mSwipRefresh.setColorSchemeColors(getResources().getColor(R.color.red));
        setRefresh();
    }

    private void setRefresh() {
        mSwipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTripList(true);
                mSwipRefresh.setRefreshing(false);
            }
        });
    }

    private void initView() {
        mTvTitle.setText("我的行程");
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        if (NetWorkUtil.isNetworkAvailable(this)) {

        }
        emptyView=findViewById(R.id.empty_view);
        mMAdapter = new TripListAdapter(this);
        mRecListTrip.setAdapter(mMAdapter);
        mRecListTrip.setEmptyView(emptyView);
        mRecListTrip.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        getTripList(false);
        mRecListTrip.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 3, getResources().getColor(R.color.red)));
    }

    /**
     * 访问网络获取行程列表信息
     */
    private void getTripList(final boolean isRefresh) {
        //String userId="99DBD039C7DCE2DA86889143946687EF6BD790241761D8CD8147EA299742DBCD6B2DC180FD294EC25F7DEBEB1F2B0DA7";
        String userId = getSharedPreferences("userinfo", MODE_PRIVATE).getString("userId", "");
        Log.i("trip", userId);
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).getTripList(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    Log.i("trip", jsonString);
                    if (null != jsonString) {
                        Gson gson = new Gson();
                        TripListBean tripListBean = gson.fromJson(jsonString, TripListBean.class);
                        if (tripListBean != null&&tripListBean.getResponseCode().equals("1")) {
                            Log.i("trip",tripListBean.getResponseObj().size()+"");
                            mMAdapter.setData(tripListBean.getResponseObj(),isRefresh);
                        }else {
                            ToastUtil.show(MyTripActivity.this,tripListBean.getResponseMsg());
                        }
                    }
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
}
