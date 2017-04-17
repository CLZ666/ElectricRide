package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.MessageBean;
import com.wanle.lequan.sharedbicycle.bean.UserInfoBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.GetUserInfo;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;


public class MyAccountActivity extends BaseActivity {

    @BindView(R.id.tv_titl)
    TextView mTvTitl;
    @BindView(R.id.tv_setting)
    TextView mTvSetting;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_fare_balance)
    TextView mTvFareBalance;
    @BindView(R.id.tv_deposit_refund)
    TextView mTvDepositRefund;
    private SharedPreferences mSpUserInfo;
    private boolean mIsDeposit;
    private double mBalance1;
    private String mBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitl.setText("我的账户");
        mTvSetting.setText("明细");
        mTvTitl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mTvSetting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getUserInfo();
        mSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mBalance = mSpUserInfo.getString("balance", "");
        mTvFareBalance.setText(mBalance);
        mIsDeposit = mSpUserInfo.getBoolean(getResources().getString(R.string.is_deposit), false);
        if (mIsDeposit){
            mTvDepositRefund.setText("押金退款");
            mTvDepositRefund.setTextColor(getResources().getColor(R.color.red));
        }else{
            mTvDepositRefund.setText("充值押金");
            mTvDepositRefund.setTextColor(getResources().getColor(R.color.colorRed));
        }
    }

    private void getUserInfo() {
        GetUserInfo.getUserInfo(this, new Subscriber<UserInfoBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UserInfoBean userInfoBean) {
                if (null != userInfoBean && userInfoBean.getResponseCode().equals("1")) {
                    int balance = userInfoBean.getResponseObj().getBalance();
                    mBalance1 = balance * 1.0 / 100;
                    mTvFareBalance.setText(mBalance1 + "");
                    final int payDeposit = userInfoBean.getResponseObj().getPayDeposit();
                    if (payDeposit > 0) {
                        mSpUserInfo.edit().putBoolean(getResources().getString(R.string.is_deposit), true);
                    }
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        initView();
    }


    @OnClick({R.id.tv_setting, R.id.tv_deposit_refund, R.id.btn_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_setting:
                startActivity(new Intent(this, RechargeRecordActivity.class));
                break;
            case R.id.tv_deposit_refund:
                if (mIsDeposit){
                    if (mBalance1>0){
                        rechargeReturn();
                    }else{
                        ToastUtils.getShortToastByString(this,"您的余额不足,不能退还押金哦!");
                    }
                }else{
                    startActivity(new Intent(this,DepositActivity.class));
                }
                break;
            case R.id.btn_recharge:
                startActivity(new Intent(this, BalanceRechargeActivity.class));
                break;
            default:
                break;
        }
    }

    private void rechargeReturn() {
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_recharge_refund, null);
        builder.setView(dialogView);
        TextView tv_confim = (TextView) dialogView.findViewById(R.id.tv_confim);
        TextView tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        tv_confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositRefund();
                builder.cancel();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });
        builder.show();
    }
    public void depositRefund(){
        String userId=mSpUserInfo.getString("userId","");
        final Call<ResponseBody> call = HttpUtil.getService(ApiService.class).depositRefund(userId);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200){
                    try {
                        final String jsonString = response.body().string();
                        if (null!=jsonString){
                           Gson gson=new Gson();
                            final MessageBean messageBean = gson.fromJson(jsonString, MessageBean.class);
                            if (messageBean.getResponseCode().equals("1")){
                                mSpUserInfo.edit().putBoolean(getResources().getString(R.string.is_deposit), false).commit();
                                mSpUserInfo.edit().putBoolean("isBorrow",false).commit();
                                ToastUtil.show(MyAccountActivity.this,"退款申请已提交");
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
}
