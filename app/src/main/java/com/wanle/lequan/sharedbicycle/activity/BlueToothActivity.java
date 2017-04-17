package com.wanle.lequan.sharedbicycle.activity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.utils.AppManage;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;

public class BlueToothActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        AppManage.addActivity(this);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        boolean isOpen =bluetoothAdapter.isEnabled();
        if (!isOpen){
            ToastUtil.show(this,"打开蓝牙才能用车哦");
        }
    }
}
