package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyLeaseActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lease);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("我的租赁");
    }

    @OnClick({R.id.iv_back, R.id.fl_cdb, R.id.fl_battery})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.fl_cdb:
                startActivity(new Intent(this,LeaseRecordActivity.class));
                break;
            case R.id.fl_battery:
                startActivity(new Intent(this,BatteryReplaceRecordActivity.class));
                break;
        }
    }
}
