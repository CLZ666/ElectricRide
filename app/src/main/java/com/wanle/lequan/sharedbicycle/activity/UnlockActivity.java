package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
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

public class UnlockActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.pb_progressbar)
    ProgressBar mPbProgressbar;
    @BindView(R.id.tv_prg)
    TextView mTvPrg;
    private int progress;
    private static final int MSG = 1;
    private SharedPreferences mSpUserInfo;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //处理消息
            switch (msg.what) {
                case MSG:
                    //设置滚动条和text的值
                    mPbProgressbar.setProgress(progress);
                    break;
                default:
                    break;
            }
        }
    };
    private Thread mUnlockThread;
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
        mCdt = new CountDownTimer(700000, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {
                userCar();
                setProgress1(10);
            }

            @Override
            public void onFinish() {
                if (!mResponseCode.equals("1")) {
                    ToastUtil.show(UnlockActivity.this, "开锁失败");
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

    @OnClick(R.id.iv_back)
    public void onClick() {
        if (progress == 100) {
            finish();
        } else {
            ToastUtil.show(this, "请耐心等待开锁完成");
        }
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
                        Log.i("unlock", jsonString);
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String responseMsg = jsonObject.getString("responseMsg");
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


    private void pbStart() {
        //子线程循环间隔消息
        mUnlockThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int max = mPbProgressbar.getMax();
                try {
                    //子线程循环间隔消息
                    while (progress < max) {
                        progress += 10;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTvPrg.setText(progress + "%");
                            }
                        });
                        userCar();
                        Message msg = new Message();
                        msg.what = MSG;
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }
                    if (progress == max) {
                        Intent intent = new Intent(UnlockActivity.this, MainActivity.class);
                        intent.putExtra("unlock", true);
                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        mUnlockThread.start();
    }
}
