package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.MessageBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 扫码开锁
 */
public class SweepLockActivity extends BaseActivity implements QRCodeView.Delegate {


    @BindView(R.id.tv_titl)
    TextView mTvTitl;
    @BindView(R.id.tv_setting)
    TextView mTvSetting;
    private static final String TAG = SweepLockActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private QRCodeView mQRCodeView;
    boolean isOpen = false;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweep_lock);
        ButterKnife.bind(this);
        initView();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    private void initView() {
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        mTvTitl.setText("扫码开锁");
        mTvSetting.setText("使用帮助");
        mTvTitl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mTvSetting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate();
        car_stuts();
    }

    private void car_stuts() {
        checkCarState();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_car_stuts, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setView(view);
        TextView tv_change = (TextView) view.findViewById(R.id.tv_change_car);
        TextView tv_unlock = (TextView) view.findViewById(R.id.tv_unlock);
        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
                mQRCodeView.startSpot();
            }
        });
        tv_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SweepLockActivity.this, UnlockActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mQRCodeView.startSpot();
    }

    public void checkCarState() {
        String userId = getSharedPreferences("userinfo", MODE_PRIVATE).getString("userId", "");
        String carNo = "24929615696887809";
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("carNo", carNo);
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).checkCarState(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    if (null != jsonString) {
                        Gson gson = new Gson();
                        MessageBean messageBean = gson.fromJson(jsonString, MessageBean.class);
                        if (messageBean.getResponseCode().equals("1")) {
                           mDialog.show();
                        }else {
                            ToastUtils.getShortToastByString(SweepLockActivity.this,messageBean.getResponseMsg());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    @OnClick({R.id.iv_manual_input, R.id.tv_manual_input, R.id.iv_open_light, R.id.tv_open_light})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_manual_input:
                startActivity(new Intent(SweepLockActivity.this, InputCodeActivity.class));
                break;
            case R.id.tv_manual_input:
                startActivity(new Intent(SweepLockActivity.this, InputCodeActivity.class));
                break;
            case R.id.iv_open_light:
                if (!isOpen) {
                    mQRCodeView.openFlashlight();
                    isOpen = true;
                } else {
                    mQRCodeView.closeFlashlight();
                    isOpen = false;
                }
                break;
            case R.id.tv_open_light:
                if (!isOpen) {
                    mQRCodeView.openFlashlight();
                    isOpen = true;
                } else {
                    mQRCodeView.closeFlashlight();
                    isOpen = false;
                }
                break;
        }
    }
}
