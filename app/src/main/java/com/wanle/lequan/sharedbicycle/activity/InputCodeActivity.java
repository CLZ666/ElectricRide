package com.wanle.lequan.sharedbicycle.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;
import com.wanle.lequan.sharedbicycle.view.PayNumberEditText;
import com.wanle.lequan.sharedbicycle.view.ProgersssDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 输入车辆编码页面
 */
public class InputCodeActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ppe_num)
    PayNumberEditText mPpeNum;
    @BindView(R.id.tv_open_light)
    TextView mTvOpenLight;
    private Camera m_camera;
    private boolean isOpen = false;
    private AlertDialog mDialog;
    private SharedPreferences mSpUserInfo;
    private ProgersssDialog mProgersssDialog;
    private CountDownTimer mCdt;
    private EditText mEditNum;
    private BlueToothControl mBlueToothControl;
    private static final String TAG = "device1";
    private boolean isFind;
    private BleManager mBleManager;
    private View mDialogView;
    private String mCarno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_code);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("输入编码");
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mPpeNum.initStyle(R.drawable.edit_num_bg, 9, 0.33f, R.color.color999999, R.color.black, 20);
        mPpeNum.setShowPwd(false);
        mEditNum = mPpeNum.getEditText();
        if (null != mEditNum) {
            showKeyboard(mEditNum);
            mEditNum.setOnKeyListener(onKey);
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_erweima, R.id.iv_open_light, R.id.tv_erweima, R.id.tv_open_light, R.id.btn_confim})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_erweima:
                startActivity(new Intent(this, SweepLockActivity.class));
                break;
            case R.id.iv_open_light:
                if (!isOpen) {
                    onLight();
                    mTvOpenLight.setText("关闭照明");
                    isOpen = true;
                } else {
                    offLight();
                    mTvOpenLight.setText("开启照明");
                    isOpen = false;
                }
                break;
            case R.id.tv_erweima:
                startActivity(new Intent(this, SweepLockActivity.class));
                break;
            case R.id.tv_open_light:
                if (!isOpen) {
                    onLight();
                    mTvOpenLight.setText("关闭照明");
                    isOpen = true;
                } else {
                    offLight();
                    mTvOpenLight.setText("开启照明");
                    isOpen = false;
                }
                break;
            case R.id.btn_confim:
                if (NetWorkUtil.isNetworkAvailable(this)) {
                   chooseService();
                    // connectBle();
                }
                break;
            default:
                break;
        }
    }

    public void showCarState() {
        mDialogView = LayoutInflater.from(InputCodeActivity.this).inflate(R.layout.dialog_car_stuts, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(InputCodeActivity.this);
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);
        TextView tv_change = (TextView) mDialogView.findViewById(R.id.tv_change_car);
        TextView tv_unlock = (TextView) mDialogView.findViewById(R.id.tv_unlock);
        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        tv_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(InputCodeActivity.this, UnlockActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void checkCarState() {
        String userId = mSpUserInfo.getString("userId", "");
        final String carNo = "181123321170";
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("carNo", carNo);
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).checkCarState(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    Log.i("distance1",jsonString);
                    if (null != jsonString) {
                        Gson gson = new Gson();
                        final CarStateCheckBean carStateCheckBean = gson.fromJson(jsonString, CarStateCheckBean.class);
                        if (null != carStateCheckBean) {
                            if (carStateCheckBean.getResponseCode().equals("1")) {
                                mCdt = new CountDownTimer(1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        mProgersssDialog.dismiss();
                                        Intent intent=new Intent(InputCodeActivity.this,EBikeStatusActivity.class);
                                        intent.putExtra("carStateCheckBean",carStateCheckBean);
                                        intent.putExtra("carNo", carNo);
                                        startActivity(intent);
                                    }
                                };
                                mCdt.start();
                            } else {
                                mProgersssDialog.dismiss();
                                ToastUtils.getShortToastByString(InputCodeActivity.this, carStateCheckBean.getResponseMsg());
                            }
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

    private void chooseService() {
        String numText = mPpeNum.getPwdText();
        mCarno = "24929615696887809";
        if (TextUtils.isEmpty(numText) || numText.length() < 9) {
            ToastUtils.getShortToastByString(this, "请确认输入的编号是否正确!");
        } else {
            mProgersssDialog = new ProgersssDialog(InputCodeActivity.this);
            mProgersssDialog.setMsg("正在获取车辆信息");
            checkCarState();
        }
    }


    public void onLight() {
        try {
            m_camera = Camera.open();
            Camera.Parameters mParameters;
            mParameters = m_camera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            m_camera.setParameters(mParameters);
        } catch (Exception ex) {
        }

    }

    public void offLight() {
        try {
            Camera.Parameters mParameters;
            mParameters = m_camera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            m_camera.setParameters(mParameters);
            m_camera.release();
        } catch (Exception ex) {
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCdt != null) {
            mCdt.cancel();
        }
    }

    protected void showKeyboard(final EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(editText, 0);
                           }

                       },
                200);
    }

    /**
     * 和车的蓝牙锁连接
     */
    public void connectBle() {
        String numText = mPpeNum.getPwdText();
        if (TextUtils.isEmpty(numText) || numText.length() < 9) {
            ToastUtils.getShortToastByString(this, "请确认输入的编号是否正确!");
        } else {
            mBlueToothControl = new BlueToothControl(this);
            mBleManager = mBlueToothControl.getBleManager();
            if (mBlueToothControl.isSupportBle()) {
                scanDevice();
            } else {
                ToastUtil.show(this, "您当前设备不支持蓝牙,无法用车");
            }
        }
    }

    /**
     * 扫描附近设备
     */
    private void scanDevice() {
        mProgersssDialog = new ProgersssDialog(InputCodeActivity.this);
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
                        chooseService();
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

    View.OnKeyListener onKey = new View.OnKeyListener() {

        @Override

        public boolean onKey(View v, int keyCode, KeyEvent event) {


            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (NetWorkUtil.isNetworkAvailable(InputCodeActivity.this)) {
                    chooseService();
                    // connectBle();
                }

                return true;

            }

            return false;

        }

    };
}
