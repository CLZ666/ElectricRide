package com.wanle.lequan.sharedbicycle.activity;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wanle.lequan.sharedbicycle.R;

public class BaseActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private ConnectivityManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

}
