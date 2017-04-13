package com.wanle.lequan.sharedbicycle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.CarState;
import com.wanle.lequan.sharedbicycle.bean.MessageBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.event.CarStateEvent;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;
import com.wanle.lequan.sharedbicycle.view.ProgersssDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * autor:Jerry
 * fuction:车辆状态的fragment
 * Date: 2017/3/12.
 */

public class CarStateFragment extends Fragment {
    @BindView(R.id.tv_car_no)
    TextView mTvCarNo;
    @BindView(R.id.tv_dump_energy)
    TextView mTvDumpEnergy;
    @BindView(R.id.tv_riding_time)
    TextView mTvRidingTime;
    @BindView(R.id.tv_ride_cost)
    TextView mTvRideCost;
    @BindView(R.id.tv_car_status)
    TextView mTvCarStatus;
    @BindView(R.id.tv_is_lock)
    TextView mTvIsLock;
    @BindView(R.id.iv_bike_state)
    ImageView mIvBikeState;

    private ProgersssDialog mProgersssDialog;
    private String mCarNo;
    private int mCarPower;
    private int mCycleTime;
    private int mCycleCharge;
    private int mUseStatus;
    private CountDownTimer mCdt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock_car, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick({R.id.tv_car_status, R.id.tv_is_lock})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_is_lock:
                if (NetWorkUtil.isNetworkAvailable(getActivity())) {
                    if (mUseStatus == 2) {
                        mProgersssDialog = new ProgersssDialog(getActivity());
                        mProgersssDialog.setMsg("正在开锁");
                        continueUse();
                    } else if (mUseStatus == 1) {
                        final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_lock_car, null);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final AlertDialog dialog = builder.create();
                        dialog.setView(dialogView);
                        TextView tv_know = (TextView) dialogView.findViewById(R.id.tv_know);
                        tv_know.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mProgersssDialog = new ProgersssDialog(getActivity());
                                mProgersssDialog.setMsg("正在锁车");
                                tempLock(mProgersssDialog);
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void tempLock(final ProgersssDialog dialog) {
        String userId = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE).getString("userId", "");
        Log.i("templock", userId);
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).tempLock(userId);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    Log.i("templock", jsonString);
                    if (null != jsonString) {
                        Gson gson = new Gson();
                        MessageBean messageBean = gson.fromJson(jsonString, MessageBean.class);
                        if (messageBean != null) {
                            String responseCode = messageBean.getResponseCode();
                            if (responseCode.equals("1")) {
                                mCdt = new CountDownTimer(1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    @Override
                                    public void onFinish() {
                                        dialog.dismiss();
                                        mUseStatus = 2;
                                        mTvIsLock.setText("继续使用");
                                        mTvCarStatus.setText("车辆锁定中");
                                        mIvBikeState.setImageResource(R.drawable.smal_lock);
                                     /*   mLinearLockTime.setVisibility(View.VISIBLE);*/
                                    }
                                };
                                mCdt.start();
                            } else {
                                ToastUtils.getShortToastByString(getActivity(), "临时锁车失败");
                                dialog.dismiss();
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

    @Subscribe
    public void onEventMainThread(CarStateEvent event) {
        CarState carState = event.getCarState();
        Log.i("carstate", carState.toString());
        mCarNo = carState.getCarNo();
        mCarPower = carState.getCarPower();
        mCycleTime = carState.getCycleTime();
        mCycleCharge = carState.getCycleCharge();
        mUseStatus = carState.getUseStatus();
        updateUi();
    }

    /**
     *
     */
    private void updateUi() {
        mTvCarNo.setText("车辆编号: " + mCarNo);
        mTvDumpEnergy.setText(mCarPower + "%");
        mTvRidingTime.setText(getStandardTime((long) (mCycleTime * 60)));
        mTvRideCost.setText("￥ " + (mCycleCharge / 100));
        if (mUseStatus == 1) {
            mTvIsLock.setText("临时锁车");
            mTvCarStatus.setText("车辆骑行中");
            mIvBikeState.setImageResource(R.drawable.using_bike_state);
           /* mLinearLockTime.setVisibility(View.GONE);*/
        } else {
            mTvIsLock.setText("继续使用");
            mTvCarStatus.setText("车辆锁定中");
            mIvBikeState.setImageResource(R.drawable.smal_lock);
           /* mLinearLockTime.setVisibility(View.VISIBLE);*/
        }
    }

    public String getStandardTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = new Date(timestamp * 1000);
        sdf.format(date);
        return sdf.format(date);
    }

    /**
     * 临时锁车后继续使用的接口
     */
    private void continueUse() {
        String userId = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE).getString("userId", "");
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).continueUse(userId);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    Log.i("continueUse", jsonString);
                    if (null != jsonString) {
                        Gson gson = new Gson();
                        MessageBean messageBean = gson.fromJson(jsonString, MessageBean.class);
                        if (null != messageBean) {
                            String responseCode = messageBean.getResponseCode();
                            if (responseCode.equals("1")) {
                                mCdt = new CountDownTimer(1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    @Override
                                    public void onFinish() {
                                        mProgersssDialog.dismiss();
                                        mUseStatus = 1;
                                        mTvIsLock.setText("临时锁车");
                                        mTvCarStatus.setText("车辆骑行中");
                                        mProgersssDialog.dismiss();
                                        mIvBikeState.setImageResource(R.drawable.using_bike_state);
                                        /*mLinearLockTime.setVisibility(View.GONE);*/
                                    }
                                };
                                mCdt.start();
                            } else {
                                ToastUtils.getShortToastByString(getActivity(), "开锁失败");
                                mProgersssDialog.dismiss();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCdt != null) {
            mCdt.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
