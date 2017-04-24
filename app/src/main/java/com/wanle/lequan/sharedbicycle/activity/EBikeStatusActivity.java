package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.CarStateCheckBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
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
    private SharedPreferences mSpUserInfo;
    private String mCarNo;

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
        mTvEbikeNo.setText("NO."+mCarNo);
    }

    @OnClick({R.id.iv_back, R.id.btn_unlock_ebike, R.id.btn_lease_mobile_power, R.id.btn_replace_battery})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_unlock_ebike:
                startActivity(new Intent(this,UnlockActivity.class));
                break;
            case R.id.btn_lease_mobile_power:
                break;
            case R.id.btn_replace_battery:
                break;
        }
    }
    public void checkCarState() {
        String userId = mSpUserInfo.getString("userId", "");
        String carNo = mCarNo;
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("carNo", carNo);
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).checkCarState(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    if (null != jsonString) {
                        Gson gson = new Gson();
                        final CarStateCheckBean carStateCheckBean = gson.fromJson(jsonString, CarStateCheckBean.class);
                        if (null != carStateCheckBean) {
                            if (carStateCheckBean.getResponseCode().equals("1")) {
                                mTvBatteryLevel.setText(carStateCheckBean.getResponseObj().getCarPower() + "%");
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
}
