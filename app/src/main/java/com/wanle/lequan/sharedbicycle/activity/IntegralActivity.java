package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntegralActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("积分");
    }

    @OnClick({R.id.iv_back, R.id.tv_look_record, R.id.iv_integral_rule, R.id.iv_illegal, R.id.tv_integral_rule, R.id.tv_illegal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_look_record:
                break;
            case R.id.iv_integral_rule:
                break;
            case R.id.iv_illegal:
                break;
            case R.id.tv_integral_rule:
                break;
            case R.id.tv_illegal:
                break;
        }
    }
}
