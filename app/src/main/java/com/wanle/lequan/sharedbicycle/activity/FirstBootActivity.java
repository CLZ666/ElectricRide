package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.FirstBootAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstBootActivity extends FragmentActivity {

    @BindView(R.id.vp_first_boot)
    ViewPager mVpFirstBoot;
    private FragmentManager mFragmentManager;
    private FirstBootAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_boot);
        ButterKnife.bind(this);
        //全屏显示内容，隐藏覆盖通知栏
        //getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        mFragmentManager = getSupportFragmentManager();
        mAdapter = new FirstBootAdapter(mFragmentManager);
        mVpFirstBoot.setAdapter(mAdapter);
    }
}
