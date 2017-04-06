package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.wanle.lequan.sharedbicycle.MainActivity;
import com.wanle.lequan.sharedbicycle.R;

public class SpalshActivity extends BaseActivity {
    //延迟三秒进入主界面
    private static final int SHOW_TIME_MIN = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
        final SharedPreferences isFirstSp = getSharedPreferences("isFirst", MODE_PRIVATE);
        final boolean isFirst = isFirstSp.getBoolean("isFirst", false);
        //全屏显示内容，隐藏覆盖通知栏
        getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFirst){
                    goFirstBoot();
                }else{
                    goHome();
                }
            }

        }, SHOW_TIME_MIN);

    }

    private void goHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    private void goFirstBoot(){
        startActivity(new Intent(this, FirstBootActivity.class));
        finish();
    }
}
