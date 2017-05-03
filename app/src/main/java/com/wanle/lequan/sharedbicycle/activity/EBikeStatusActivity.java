package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.CarStateCheckBean;
import com.wanle.lequan.sharedbicycle.bean.CdbBatteryBorrowReturnBean;
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
    @BindView(R.id.linear_riding_distance)
    LinearLayout mLinearRidingDistance;
    @BindView(R.id.tv_battery)
    TextView mTvBattery;
    @BindView(R.id.tv_cdb)
    TextView mTvCdb;
    private SharedPreferences mSpUserInfo;
    private String mCarNo;
    private CarStateCheckBean mCarStateCheckBean;
    private int mBatteryStatus;
    private int mCdbStatus;
    private int mPowerCount;
    private String mMaxPowerCount;
    private int mBatteryCount;
    private String mMaxBatteryCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebike_status);
        ButterKnife.bind(this);
        initView();
        //  checkCarState();
    }

    private void initView() {
        mTvTitle.setText("选择服务");
        mSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mCarNo = getIntent().getStringExtra("carNo");
        mTvEbikeNo.setText("NO." + mCarNo);
        mCarStateCheckBean = (CarStateCheckBean) getIntent().getSerializableExtra("carStateCheckBean");
        if (null != mCarStateCheckBean) {
            //车辆内置电池的可使用情况
            mBatteryStatus = mCarStateCheckBean.getResponseObj().getBattery().getBatteryStatus();
            //车辆内置充电宝的可使用情况
            mCdbStatus = mCarStateCheckBean.getResponseObj().getPowerBank().getPowerBankStatus();
            //更新ui
            if (mBatteryStatus == 1) {
                mTvRideDistance.setText(mCarStateCheckBean.getResponseObj().getBattery().getDistance() / 1000 + "km");
                mTvBatteryLevel.setText(mCarStateCheckBean.getResponseObj().getBattery().getCarPower() + "%");
                mTvReplaceDc.setText("取电池");
            } else if (mBatteryStatus == 2) {
                mLinearRidingDistance.setVisibility(View.GONE);
                mTvBatteryLevel.setVisibility(View.GONE);
                mTvReplaceDc.setText("取电池");
            } else {
                mLinearRidingDistance.setVisibility(View.GONE);
                mTvBatteryLevel.setVisibility(View.GONE);
                mTvBattery.setText("该车辆暂无电池");
                mTvReplaceDc.setText("还电池");
            }
            if (mCdbStatus == 1) {
                mTvMobilePowerLevel.setText(mCarStateCheckBean.getResponseObj().getPowerBank().getPowerElectricity() + "%");
                mTvLeaseCdb.setText("租充电宝");
            } else if (mCdbStatus == 2) {
                mTvMobilePowerLevel.setVisibility(View.GONE);
                mTvCdb.setText("内置充电宝暂时无法使用");
                mTvLeaseCdb.setText("租充电宝");
            } else {
                mTvMobilePowerLevel.setVisibility(View.GONE);
                mTvCdb.setText("该车辆暂无充电宝");
                mTvLeaseCdb.setText("还充电宝");
            }
            mPowerCount = mCarStateCheckBean.getResponseObj().getCarResult().getPowerCount();
            mMaxPowerCount = mCarStateCheckBean.getResponseObj().getCarResult().getMaxPowerCount();

            mBatteryCount = mCarStateCheckBean.getResponseObj().getCarResult().getBatteryCount();
            mMaxBatteryCount = mCarStateCheckBean.getResponseObj().getCarResult().getMaxBatteryCount();
        }

    }


    @OnClick({R.id.iv_back, R.id.linear_unlock, R.id.linear_lease_cdb, R.id.linear_replace_dc, R.id.linear_service_desc})
    public void onClick(View view) {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            switch (view.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.linear_unlock:
                    toUnLock();
                    break;
                case R.id.linear_lease_cdb:
                    final String s = mTvLeaseCdb.getText().toString();
                    if (s.equals("租充电宝")) {
                        toLeaseCdb();
                    } else {
                        toReturnCdb();
                    }
                    //ToastUtils.getShortToastByString(this, "敬请期待");
                    break;
                case R.id.linear_replace_dc:
                    final String s1 = mTvReplaceDc.getText().toString().trim();
                    if (s1.equals("取电池")) {
                        toReplaceBattery();
                    } else {
                        toReturnBattery();
                    }
                    // ToastUtils.getShortToastByString(this, "敬请期待");
                    break;
                case R.id.linear_service_desc:
                    ToastUtils.getShortToastByString(this, "敬请期待");
                    break;
            }
        }
    }

    /**
     * 租用充电宝前的判断
     */
    private void toLeaseCdb() {
        if (null != mCarStateCheckBean) {
            if (mCdbStatus == 1) {
                if (mPowerCount < Integer.parseInt(mMaxPowerCount)) {
                    ToastUtil.show(this, "可以租用充电宝了");
                    leaseReturnCdb();
                } else {
                    ToastUtil.show(this, "您已租用一块充电宝，暂时无法租用!");
                }
            } else if (mCdbStatus == 2) {
                ToastUtil.show(this, "车内充电宝已损坏，暂时无法租用");
            } else {
                ToastUtil.show(this, "车内暂无充电宝，无法租用");
            }
        }
    }

    /**
     * 租还充电宝的接口
     */
    public void leaseReturnCdb() {
        Map<String, String> map = new HashMap<>();
        String userId = getSharedPreferences("userinfo", MODE_PRIVATE).getString("userId", "");
        map.put("userId", userId);
        map.put("carNo", mCarNo);
        final Call<ResponseBody> call = HttpUtil.getService(ApiService.class).leaseReturnCdb(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        final String jsonString = response.body().string();
                        if (null != jsonString) {
                            Gson gson = new Gson();
                            final CdbBatteryBorrowReturnBean bean = gson.fromJson(jsonString, CdbBatteryBorrowReturnBean.class);
                            if (null != bean) {
                                if (bean.getResponseCode().equals("1")) {
                                    if (bean.getResponseObj().getOperateType() == 0) {
                                        ToastUtil.show(EBikeStatusActivity.this, "充电宝租用成功");
                                    } else {
                                        ToastUtil.show(EBikeStatusActivity.this, "还充电宝成功");
                                    }
                                } else {
                                    ToastUtil.show(EBikeStatusActivity.this, bean.getResponseMsg());
                                }
                            }
                        }
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

    /**
     * 借还车辆电池的接口
     */
    public void replaceReturnBattery() {
        Map<String, String> map = new HashMap<>();
        String userId = getSharedPreferences("userinfo", MODE_PRIVATE).getString("userId", "");
        map.put("userId", userId);
        map.put("carNo", mCarNo);
        final Call<ResponseBody> call = HttpUtil.getService(ApiService.class).replaceReturnBattery(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        final String jsonString = response.body().string();
                        if (null != jsonString) {
                            Gson gson = new Gson();
                            final CdbBatteryBorrowReturnBean bean = gson.fromJson(jsonString, CdbBatteryBorrowReturnBean.class);
                            if (null != bean) {
                                if (bean.getResponseCode().equals("1")) {
                                    if (bean.getResponseObj().getOperateType() == 0) {
                                        ToastUtil.show(EBikeStatusActivity.this, "电池取出成功");
                                    } else {
                                        ToastUtil.show(EBikeStatusActivity.this, "还电池成功");
                                    }
                                } else {
                                    ToastUtil.show(EBikeStatusActivity.this, bean.getResponseMsg());
                                }
                            }
                        }
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

    /**
     * 还充电宝前的判断
     */
    private void toReturnCdb() {
        if (null != mCarStateCheckBean) {
            if (mCdbStatus == 0) {
                if (mPowerCount > 0) {
                    ToastUtil.show(this, "可以还充电宝了");
                    leaseReturnCdb();
                } else {
                    ToastUtil.show(this, "您还没有租用充电宝哦");
                }
            }
        }
    }

    /**
     * 取电池前的判断
     */
    public void toReplaceBattery() {
        if (null != mCarStateCheckBean) {
            if (mBatteryStatus == 1) {
                final int carPower = mCarStateCheckBean.getResponseObj().getBattery().getCarPower();
                if (mBatteryCount < Integer.parseInt(mMaxBatteryCount)) {
                    if (carPower <= 10) {
                        replaceReturnBattery();
                        ToastUtil.show(this, "可以取电池了");
                    } else {
                        ToastUtil.show(this, "电池电量充足，不用更换");
                    }
                } else {
                    ToastUtil.show(this, "您已拥有2块电池，暂时不能取出电池");
                }
            } else {
                ToastUtil.show(this, "车内电池已损坏，无法取出");
            }
        }
    }

    /**
     * 还电池前的判断
     */
    public void toReturnBattery() {
        if (null != mCarStateCheckBean) {
            if (mBatteryCount > 0) {
                replaceReturnBattery();
                ToastUtil.show(this, "您已可以还电池了");
            } else {
                ToastUtil.show(this, "您还未拥有电池哦！");
            }
        }

    }

    /**
     * 开锁前的判断
     */
    private void toUnLock() {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            if (null != mCarStateCheckBean) {
                if (mBatteryStatus == 1) {
                    final String minCarPower = mCarStateCheckBean.getResponseObj().getCarResult().getMinCarPower();
                    final int carPower = mCarStateCheckBean.getResponseObj().getBattery().getCarPower();
                    if (carPower >= Integer.parseInt(minCarPower)) {
                        startActivity(new Intent(EBikeStatusActivity.this, UnlockActivity.class));
                        finish();
                    } else {
                        ToastUtil.show(EBikeStatusActivity.this, "车辆电池电量过低，暂时无法使用");
                    }
                } else if (mBatteryStatus == 2) {
                    ToastUtil.show(EBikeStatusActivity.this, "车辆电池已损坏，暂时无法使用");
                } else {
                    ToastUtil.show(EBikeStatusActivity.this, "车辆内无电池，暂时无法使用");
                }
            }
        }
    }
}
