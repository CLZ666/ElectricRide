package com.wanle.lequan.sharedbicycle.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleGattCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.ListScanCallback;
import com.clj.fastble.utils.HexUtil;
import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.CarStateCheckBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.BlueToothControl;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;
import com.wanle.lequan.sharedbicycle.view.ProgersssDialog;

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

    private QRCodeView mQRCodeView;
    boolean isOpen = false;
    private AlertDialog mDialog;
    private BlueToothControl mBlueToothControl;
    private static final String TAG = "device1";
    private boolean isFind;
    private BleManager mBleManager;
    private ProgersssDialog mProgersssDialog;

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
        //connectBle();
    }

    private void car_stuts() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_car_stuts, null);
        checkCarState(view);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);
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

    public void checkCarState(final View dialogView) {
        final TextView tv_car_power = (TextView) dialogView.findViewById(R.id.tv_car_power);
        final TextView tv_distance = (TextView) dialogView.findViewById(R.id.tv_distance);
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
                    Log.i("checkcarState", jsonString);
                    if (null != jsonString) {
                        Gson gson = new Gson();
                        CarStateCheckBean carStateCheckBean = gson.fromJson(jsonString, CarStateCheckBean.class);
                        if (carStateCheckBean.getResponseCode().equals("1")) {
                            tv_car_power.setText(carStateCheckBean.getResponseObj().getCarPower()+"%");
                            tv_distance.setText(carStateCheckBean.getResponseObj().getDistance()/1000+"km");
                            mDialog.setView(dialogView);
                            mDialog.show();
                        } else {
                            ToastUtils.getShortToastByString(SweepLockActivity.this, carStateCheckBean.getResponseMsg());
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
            case R.id.tv_open_light:
                if (!isOpen) {
                    mQRCodeView.openFlashlight();
                    isOpen = true;
                } else {
                    mQRCodeView.closeFlashlight();
                    isOpen = false;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 和车的蓝牙锁连接
     */
    public void connectBle() {
        mBlueToothControl = new BlueToothControl(this);
        mBleManager = mBlueToothControl.getBleManager();
        if (mBlueToothControl.isSupportBle()) {
            scanDevice();
        } else {
            ToastUtil.show(this, "您当前设备不支持蓝牙,无法用车");
        }
    }

    /**
     * 扫描附近设备
     */
    private void scanDevice() {
        mProgersssDialog = new ProgersssDialog(SweepLockActivity.this);
        mProgersssDialog.setMsg("正在获取车辆信息");
        mBlueToothControl.scanDevice(new ListScanCallback(5000) {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                super.onLeScan(device, rssi, scanRecord);
               /* Log.i(TAG,device.getAddress());
                final String mac = device.getAddress();
                if (null != mac && "F0:C7:7F:F9:C9:C4".equals(mac)) {
                    Log.i(TAG,device.getAddress());
                    // connectByName(name);
                    connectByMac(mac);
                    isFind = true;
                }*/
            }

            @Override
            public void onDeviceFound(BluetoothDevice[] devices) {
                Log.i(TAG, "共发现" + devices.length + "台设备");
                for (int i = 0; i < devices.length; i++) {
                    // final String name = devices[i].getName();
                    final String mac = devices[i].getAddress();
                    if (null != mac && "F0:C7:7F:F9:C9:C4".equals(mac)) {
                        Log.i(TAG, devices[i].getAddress());
                        // connectByName(name);
                        connectByMac(mac);
                        isFind = true;
                    }
                }
                if (!isFind) {
                    scanDevice();
                }
            }
        });
    }

    /**
     * 根据蓝牙设备的mac地址连接
     *
     * @param mac
     */
    private void connectByMac(String mac) {
        mBlueToothControl.connectByMac(mac, (long) 2000, new BleGattCallback() {
            @Override
            public void onNotFoundDevice() {
                Log.i(TAG, "未发现设备！");
                scanDevice();
            }

            @Override
            public void onFoundDevice(BluetoothDevice device) {
                Log.i(TAG, "发现设备: " + device.getAddress());
            }

            @SuppressLint("NewApi")
            @Override
            public void onConnectSuccess(BluetoothGatt gatt, int status) {

                Log.i(TAG, "连接成功！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        car_stuts();
                    }
                });
                gatt.discoverServices();
            }

            @SuppressLint("NewApi")
            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                Log.i(TAG, "服务被发现！");
                mBleManager.getBluetoothState();

                for (final BluetoothGattService service : gatt.getServices()) {
                    Log.i("TAG1", service.getUuid().toString());
                    for (final BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        Log.i(TAG, characteristic.getUuid().toString());
                        if (null != characteristic.getValue()) {
                            Log.i(TAG, String.valueOf(HexUtil.encodeHex(characteristic.getValue())));
                        }
                        final int properties = characteristic.getProperties();
                        Log.i(TAG, properties + "");
                    }
                }
            }


            @Override
            public void onConnectFailure(BleException exception) {
                Log.i(TAG, "连接失败或连接中断：" + exception.toString());
                mBleManager.handleException(exception);
            }
        });
    }
}
