package com.wanle.lequan.sharedbicycle.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.RechargeRecordAdapter;
import com.wanle.lequan.sharedbicycle.bean.RechargeRecordBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RechargeRecordActivity extends AppCompatActivity {

    @BindView(R.id.tv_titl)
    TextView mTvTitl;
    @BindView(R.id.tv_setting)
    TextView mTvSetting;
    @BindView(R.id.lv_recharge_record)
    ListView mLvRechargeRecord;
    @BindView(R.id.swip_refresh)
    SwipeRefreshLayout mSwipRefresh;
    private SharedPreferences mMSpUserInfo;
    private String mUserId;
    private RechargeRecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_record);
        ButterKnife.bind(this);
        if (NetWorkUtil.isNetworkAvailable(this)) {

        }
        initView();

        getRecord(false);
        mSwipRefresh.setColorSchemeColors(getResources().getColor(R.color.red));
        mSwipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecord(true);
                mSwipRefresh.setRefreshing(false);
            }
        });
    }

    private void initView() {
        mTvTitl.setText("账户明细");
        mTvSetting.setText("退款说明");
        mTvTitl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mTvSetting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        mMSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mUserId = mMSpUserInfo.getString("userId", "");
        mAdapter = new RechargeRecordAdapter(this);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty_view, null);
        TextView tv_empty = (TextView) emptyView.findViewById(R.id.tv_empty);
        tv_empty.setText("暂时没有充值记录哦");
        mLvRechargeRecord.setEmptyView(emptyView);
        mLvRechargeRecord.setAdapter(mAdapter);
    }

    private void getRecord(final boolean isRefresh) {
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).rechargeRecord(mUserId);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    if (jsonString != null) {
                        Gson gson = new Gson();
                        RechargeRecordBean dataBean = gson.fromJson(jsonString, RechargeRecordBean.class);
                        if (null != dataBean) {
                            mAdapter.setData(dataBean.getResponseObj().getList(), isRefresh);
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
