package com.wanle.lequan.sharedbicycle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import com.wanle.lequan.sharedbicycle.MainActivity;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.event.MyEvent;
import com.wanle.lequan.sharedbicycle.receiver.NetInfoReceiver;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;

import org.greenrobot.eventbus.Subscribe;

public class SpalshActivity extends BaseActivity {
    //延迟三秒进入主界面
    private static final int SHOW_TIME_MIN = 3000;
    private boolean mIsFirst;
    private NetInfoReceiver mNetinfoReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
        final SharedPreferences isFirstSp = getSharedPreferences("isFirst", MODE_PRIVATE);
        mIsFirst = isFirstSp.getBoolean("isFirst", false);
        //全屏显示内容，隐藏覆盖通知栏
        getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        monitiorNetInfo();
        if (!NetWorkUtil.isNetworkAvailable1(this)) {

        } else {
            nextStep(mIsFirst);
        }
    }
    /**
     * 监测网络连接状态
     */
    private void monitiorNetInfo() {
        mNetinfoReceiver = new NetInfoReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetinfoReceiver, filter1);
    }

    /**
     * 获得网络信息接受者传来的消息
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MyEvent event) {
        String msg = event.getMsg();
        // Log.i("eventmsg", msg);
        if (msg != null) {
            if (msg.equals("网络连接成功")) {
               nextStep(mIsFirst);
            }
        }
    }
    private void nextStep(final boolean isFirst) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFirst) {
                    goFirstBoot();
                } else {
                    goHome();
                }
            }

        }, SHOW_TIME_MIN);
    }


    private void openNetSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("网络无法访问，检查网络连接");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                    intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        builder.show();
    }

    private void goHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void goFirstBoot() {
        startActivity(new Intent(this, FirstBootActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetinfoReceiver);
    }
}
