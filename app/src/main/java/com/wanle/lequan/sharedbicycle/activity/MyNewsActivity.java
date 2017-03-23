package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyNewsActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_news);
        ButterKnife.bind(this);
        mTvTitle.setText("我的消息");
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
