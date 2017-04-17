package com.wanle.lequan.sharedbicycle.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_about_us)
    TextView mTvAboutUs;
    private SharedPreferences mSpGlobalParms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        mSpGlobalParms=getSharedPreferences("global",MODE_PRIVATE);
        final String aboutUs = mSpGlobalParms.getString("aboutUs", "");
        mTvTitle.setText("关于我们");
        mTvAboutUs.setText(aboutUs);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
