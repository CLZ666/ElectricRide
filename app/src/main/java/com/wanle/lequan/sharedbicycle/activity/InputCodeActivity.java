package com.wanle.lequan.sharedbicycle.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.MessageBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;
import com.wanle.lequan.sharedbicycle.view.PayNumberEditText;
import com.wanle.lequan.sharedbicycle.view.ProgersssDialog;

import java.io.IOException;
import java.util.ArrayList;
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
public class InputCodeActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ppe_num)
    PayNumberEditText mPpeNum;
    private Camera m_camera;
    private boolean isOpen = false;
    private ArrayList<EditText> edits = new ArrayList<>();
    private AlertDialog mDialog;
    private SharedPreferences mSpUserInfo;
    private ProgersssDialog mProgersssDialog;
    private CountDownTimer mCdt;
    private EditText mEditNum;

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
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_erweima, R.id.iv_open_light, R.id.tv_erweima, R.id.v_open_light, R.id.btn_confim})
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
                    isOpen = true;
                } else {
                    offLight();
                    isOpen = false;
                }
                break;
            case R.id.tv_erweima:
                startActivity(new Intent(this, SweepLockActivity.class));
                break;
            case R.id.v_open_light:
                if (!isOpen) {
                    onLight();
                    isOpen = true;
                } else {
                    offLight();
                    isOpen = false;
                }
                break;
            case R.id.btn_confim:
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    unLockCar();
                }
                break;
        }
    }

    public void showCarState() {
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
            }
        });
        tv_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputCodeActivity.this, UnlockActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void checkCarState() {
        String userId = mSpUserInfo.getString("userId", "");
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
                        if (null != messageBean) {
                            if (messageBean.getResponseCode().equals("1")) {
                                mCdt = new CountDownTimer(1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        mProgersssDialog.dismiss();
                                        mDialog.show();
                                    }
                                };
                                mCdt.start();
                            } else {
                                mProgersssDialog.dismiss();
                                ToastUtils.getShortToastByString(InputCodeActivity.this, messageBean.getResponseMsg());
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

    private void unLockCar() {
        String numText = mPpeNum.getPwdText();
        if (TextUtils.isEmpty(numText) || numText.length() < 9) {
            ToastUtils.getShortToastByString(this, "请确认输入的编号是否正确!");
        } else {
            mProgersssDialog = new ProgersssDialog(InputCodeActivity.this);
            mProgersssDialog.setMsg("正在获取车辆信息");
            showCarState();
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

}
