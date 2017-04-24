package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyLeaseActivity extends AppCompatActivity {

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
                break;
            case R.id.fl_battery:
                break;
        }
    }
}
