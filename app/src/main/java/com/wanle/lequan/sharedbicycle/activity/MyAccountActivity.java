package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.UserInfoBean;
import com.wanle.lequan.sharedbicycle.utils.GetUserInfo;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;


public class MyAccountActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);
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
        String balance = mSpUserInfo.getString("balance", "");
        mTvFareBalance.setText(balance);
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
                int balance = userInfoBean.getResponseObj().getBalance();
                double mbalance = balance * 1.0 / 100;
                mTvFareBalance.setText(mbalance + "");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }


    @OnClick({R.id.tv_setting, R.id.tv_deposit_refund, R.id.btn_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_setting:
                startActivity(new Intent(this, RechargeRecordActivity.class));
                break;
            case R.id.tv_deposit_refund:
                ToastUtil.show(this,"暂时不支持押金退款哦");
                break;
            case R.id.btn_recharge:
                startActivity(new Intent(this, BalanceRechargeActivity.class));
                break;
        }
    }
}
