package com.wanle.lequan.sharedbicycle.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
    @BindView(R.id.edit_1)
    EditText mEdit1;
    @BindView(R.id.edit_2)
    EditText mEdit2;
    @BindView(R.id.edit_3)
    EditText mEdit3;
    @BindView(R.id.edit_4)
    EditText mEdit4;
    @BindView(R.id.edit_5)
    EditText mEdit5;
    @BindView(R.id.edit_6)
    EditText mEdit6;
    @BindView(R.id.edit_7)
    EditText mEdit7;
    @BindView(R.id.edit_8)
    EditText mEdit8;
    @BindView(R.id.edit_9)
    EditText mEdit9;
    private Camera m_camera;
    private boolean isOpen = false;
    private ArrayList<EditText> edits = new ArrayList<>();
    private AlertDialog mDialog;
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == 1) {
                if (mEdit1.isFocused()) {
                    mEdit1.clearFocus();
                    mEdit2.requestFocus();
                } else if (mEdit2.isFocused()) {
                    mEdit2.clearFocus();
                    mEdit3.requestFocus();
                } else if (mEdit3.isFocused()) {
                    mEdit3.clearFocus();
                    mEdit4.requestFocus();
                } else if (mEdit4.isFocused()) {
                    mEdit4.clearFocus();
                    mEdit5.requestFocus();
                } else if (mEdit5.isFocused()) {
                    mEdit5.clearFocus();
                    mEdit6.requestFocus();
                } else if (mEdit6.isFocused()) {
                    mEdit6.clearFocus();
                    mEdit7.requestFocus();
                } else if (mEdit7.isFocused()) {
                    mEdit7.clearFocus();
                    mEdit8.requestFocus();
                } else if (mEdit8.isFocused()) {
                    mEdit8.clearFocus();
                    mEdit9.requestFocus();
                } else if (mEdit9.isFocused()) {
                    mEdit9.clearFocus();
                }
            }
        }
    };
    private SharedPreferences mSpUserInfo;
    private ProgersssDialog mProgersssDialog;
    private CountDownTimer mCdt;

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
        mEdit1.setFocusable(true);
        mEdit1.setFocusableInTouchMode(true);
        mEdit1.requestFocus();
        edits.add(mEdit1);
        edits.add(mEdit2);
        edits.add(mEdit3);
        edits.add(mEdit4);
        edits.add(mEdit5);
        edits.add(mEdit6);
        edits.add(mEdit7);
        edits.add(mEdit8);
        edits.add(mEdit9);
        for (int i = 0; i < edits.size(); i++) {
            edits.get(i).setInputType(EditorInfo.TYPE_CLASS_PHONE);
            edits.get(i).addTextChangedListener(mTextWatcher);
        }
        showKeyboard(mEdit1);
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
        String no1 = mEdit1.getText().toString().trim();
        String no2 = mEdit2.getText().toString().trim();
        String no3 = mEdit3.getText().toString().trim();
        String no4 = mEdit4.getText().toString().trim();
        String no5 = mEdit5.getText().toString().trim();
        String no6 = mEdit6.getText().toString().trim();
        String no7 = mEdit7.getText().toString().trim();
        String no8 = mEdit8.getText().toString().trim();
        String no9 = mEdit9.getText().toString().trim();
        if (TextUtils.isEmpty(no1) || TextUtils.isEmpty(no2) ||
                TextUtils.isEmpty(no3) || TextUtils.isEmpty(no4) ||
                TextUtils.isEmpty(no5) || TextUtils.isEmpty(no6) ||
                TextUtils.isEmpty(no7) || TextUtils.isEmpty(no8) ||
                TextUtils.isEmpty(no9)) {
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
        if (mCdt!=null){
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
