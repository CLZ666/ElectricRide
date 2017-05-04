package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.MainActivity;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnlockActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.pb_progressbar)
    ProgressBar mPbProgressbar;
    @BindView(R.id.tv_prg)
    TextView mTvPrg;
    @BindView(R.id.iv_ad2)
    ImageView mIvAd2;
    private int progress;
    private static final int MSG = 1;
    private SharedPreferences mSpUserInfo;

    private String mResponseCode = "";
    private CountDownTimer mCdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        ButterKnife.bind(this);
        initView();
        //pbStart();
        unlock();
    }

    private void unlock() {
        mCdt = new CountDownTimer(60001, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                userCar();
                setProgress1(2);
            }

            @Override
            public void onFinish() {
                if (!mResponseCode.equals("1")) {
                    ToastUtil.show(UnlockActivity.this, "开锁超时，请换一辆车");
                    // startActivity(new Intent(UnlockActivity.this,MainActivity.class));
                    finish();
                }
            }
        };
        mCdt.start();
    }

    private void setProgress1(int prg) {
        if (progress < 100) {
            progress += prg;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvPrg.setText(progress + "%");
                }
            });
            mPbProgressbar.setProgress(progress);
        }
    }

    private void initView() {
        mTvTitle.setText("正在开锁");
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
    }

    private void userCar() {
        String userId = mSpUserInfo.getString("userId", "");
        Log.i("unlock", userId);
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("carNo", "24929615696887809");
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).userCar(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    if (null != jsonString) {
                        Log.i("unlock1", jsonString);
                        JSONObject jsonObject = new JSONObject(jsonString);
                        mResponseCode = jsonObject.getString("responseCode");
                        if (mResponseCode.equals("1")) {
                            mCdt.cancel();
                            final int tempPrg = (100 - progress) / 5;
                            CountDownTimer cdt = new CountDownTimer(6000, 1000) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    setProgress1(tempPrg);
                                }

                                @Override
                                public void onFinish() {
                                    Intent intent = new Intent(UnlockActivity.this, MainActivity.class);
                                    intent.putExtra("unlock", true);
                                    startActivity(intent);
                                    finish();
                                }
                            };
                            cdt.start();
                        } /*else {
                            ToastUtil.show(UnlockActivity.this, responseMsg);
                        }*/
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (progress == 100) {
            finish();
        } else {
            ToastUtil.show(this, "请耐心等待开锁完成");
        }
    }


    @OnClick({R.id.iv_back, R.id.iv_ad2, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (progress == 100) {
                    finish();
                } else {
                    ToastUtil.show(this, "请耐心等待开锁完成");
                }
                break;
            case R.id.iv_ad2:
                ToastUtil.show(this, "广告2");
                break;
            case R.id.btn_cancel:
                mIvAd2.setVisibility(View.GONE);
                break;
        }
    }
}
