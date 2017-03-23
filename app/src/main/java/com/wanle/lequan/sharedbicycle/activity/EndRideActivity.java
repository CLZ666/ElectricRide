package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EndRideActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_ride_cost)
    TextView mTvRideCost;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.tv_ride_time)
    TextView mTvRideTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_ride);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("骑行结束");
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
    }

    @OnClick({R.id.iv_back, R.id.tv_trip_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                /*Intent intent=new Intent(this, MainActivity.class);
                intent.putExtra("unlock",false);
                startActivity(intent);*/
                finish();
                break;
            case R.id.tv_trip_detail:
                startActivity(new Intent(this,TripDetailActivity.class).putExtra("itineraryId","bc80e9fcf7e411e6b1218e34576bcc2e"));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent=new Intent(this, MainActivity.class);
        intent.putExtra("unlock",false);
        startActivity(intent);*/
        finish();
    }
}
