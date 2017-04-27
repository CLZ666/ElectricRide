package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.CarStateCheckBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
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

public class EBikeStatusActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_ebike_no)
    TextView mTvEbikeNo;
    @BindView(R.id.tv_battery_level)
    TextView mTvBatteryLevel;
    @BindView(R.id.tv_mobile_power_level)
    TextView mTvMobilePowerLevel;
    @BindView(R.id.tv_unlock_use)
    TextView mTvUnlockUse;
    @BindView(R.id.tv_lease_cdb)
    TextView mTvLeaseCdb;
    @BindView(R.id.tv_replace_dc)
    TextView mTvReplaceDc;
    @BindView(R.id.tv_ride_distance)
    TextView mTvRideDistance;
    private SharedPreferences mSpUserInfo;
    private String mCarNo;
    private CarStateCheckBean mCarStateCheckBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebike_status);
        ButterKnife.bind(this);
        initView();
        checkCarState();
    }

    private void initView() {
        mTvTitle.setText("选择服务");
        mSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mCarNo = getIntent().getStringExtra("carNo");
        mTvEbikeNo.setText("NO." + mCarNo);
        mCarStateCheckBean = (CarStateCheckBean) getIntent().getSerializableExtra("carStateCheckBean");
        if (null != mCarStateCheckBean) {
            mTvRideDistance.setText(mCarStateCheckBean.getResponseObj().getBattery().getDistance() / 1000 + "km");
            mTvBatteryLevel.setText(mCarStateCheckBean.getResponseObj().getBattery().getCarPower() + "%");

        }
    }


    public void checkCarState() {
        String userId = mSpUserInfo.getString("userId", "");
        String carNo = mCarNo;
        carNo = "181139437395";
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("carNo", carNo);
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).checkCarState(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    Log.i("carstate", jsonString);
                    if (null != jsonString) {
                        Gson gson = new Gson();
                        final CarStateCheckBean carStateCheckBean = gson.fromJson(jsonString, CarStateCheckBean.class);
                        if (null != carStateCheckBean) {
                            if (carStateCheckBean.getResponseCode().equals("1")) {
                                // mTvBatteryLevel.setText(carStateCheckBean.getResponseObj().getCarPower() + "%");
                            } else {

                                ToastUtils.getShortToastByString(EBikeStatusActivity.this, carStateCheckBean.getResponseMsg());
                            }
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

    @OnClick({R.id.iv_back, R.id.linear_unlock, R.id.linear_lease_cdb, R.id.linear_replace_dc, R.id.linear_service_desc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.linear_unlock:
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    startActivity(new Intent(this, UnlockActivity.class));
                }
                break;
            case R.id.linear_lease_cdb:
                ToastUtils.getShortToastByString(this, "敬请期待");
                break;
            case R.id.linear_replace_dc:
                ToastUtils.getShortToastByString(this, "敬请期待");
                break;
            case R.id.linear_service_desc:
                ToastUtils.getShortToastByString(this, "敬请期待");
                break;
        }
    }
}
