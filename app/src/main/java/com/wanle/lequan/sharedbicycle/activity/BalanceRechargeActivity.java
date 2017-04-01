package com.wanle.lequan.sharedbicycle.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.RecargeAmountAdapter;
import com.wanle.lequan.sharedbicycle.bean.PayResult;
import com.wanle.lequan.sharedbicycle.bean.WxOrderInfoBean;
import com.wanle.lequan.sharedbicycle.bean.ZfbOrderInfoBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.event.MyEvent;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * 余额充值页面
 */
public class BalanceRechargeActivity extends AppCompatActivity {
    private static final int SDK_PAY_FLAG = 1;
    private static final String APP_ID = "wx1902de5450f7a61e";
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    @BindView(R.id.edit_amount)
    EditText mEditAmount;
    @BindView(R.id.rb_zfb)
    RadioButton mRbZfb;
    private SharedPreferences mSp_UserInfo;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult result = new PayResult((Map<String, String>) msg.obj);
            String memo = result.getMemo();
            Log.i("deposit", memo);
            if (memo.equals("操作已经取消。")) {
                ToastUtils.getShortToastByString(BalanceRechargeActivity.this, "操作已经取消");
            } else if (memo.equals("")) {
                ToastUtils.getShortToastByString(BalanceRechargeActivity.this, "支付成功");
                BalanceRechargeActivity.this.finish();
            }
        }

        ;
    };
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rec_recharge)
    RecyclerView mRecRecharge;
    @BindView(R.id.rgb_way_pay)
    RadioGroup mRgbWayPay;
    private RecargeAmountAdapter mAdapter;
    private String mAmount = "";
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String s1 = mEditAmount.getText().toString();
            if (!TextUtils.isEmpty(s1)) {
                mAdapter.changeTextColor("editNotEmpty");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String s1 = mEditAmount.getText().toString();
            if (!TextUtils.isEmpty(s1)) {
                mAdapter.changeTextColor("editNotEmpty");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_recharge);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        msgApi.registerApp(APP_ID);
        mSp_UserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mTvTitle.setText("余额充值");
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mRecRecharge.setHasFixedSize(true);
        mRecRecharge.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new RecargeAmountAdapter(this);
        mRecRecharge.setAdapter(mAdapter);
        mAdapter.setData(getData());
        mRbZfb.setChecked(true);
        EventBus.getDefault().register(this);
        showKeyboard(mEditAmount);
        mEditAmount.addTextChangedListener(mTextWatcher);
        mEditAmount.setOnKeyListener(onKey);
    }

    public List<String> getData() {
        List<String> data = new ArrayList<>();
        data.add("30元");
        data.add("50元");
        data.add("100元");
        data.add("150元");
        return data;
    }

    @OnClick({R.id.iv_back, R.id.btn_confim})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confim:
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    int id = mRgbWayPay.getCheckedRadioButtonId();
                    if (id == R.id.rb_zfb) {
                        recharge(id);
                    } else if (id == R.id.rb_wx) {
                        recharge(id);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(MyEvent event) {
        mAmount = event.getMsg();
        if (!mAmount.equals("")) {
            mEditAmount.setText("");
        }
    }

    /**
     * 余额充值
     */
    public void recharge(int id) {
        switch (id) {
            case R.id.rb_zfb:
                getPayData(2);
                break;
            case R.id.rb_wx:
                getPayData(1);
                break;
            default:
                break;
        }

    }

    /**
     * 访问网络，获得支付接口返回的数据
     */
    public void getPayData(final int type) {
        String amount = mEditAmount.getText().toString();
        Log.i("111", mAmount);
        if (TextUtils.isEmpty(mAmount) && TextUtils.isEmpty(amount)) {
            ToastUtil.show(BalanceRechargeActivity.this, "请输入充值金额");
            return;
        }
        if (TextUtils.isEmpty(amount)) {
            if (mAmount.equals("30元")) {
                amount = (30 * 100) + "";
            } else if (mAmount.equals("50元")) {
                amount = "" + (50 * 100);
            } else if (mAmount.equals("100元")) {
                amount = "" + (100 * 100);
            } else if (mAmount.equals("150元")) {
                amount = "" + (150 * 100);
            }
        } else {
            int i = Integer.parseInt(amount);
            amount = "" + (i * 100);
        }
        Map<String, String> map = new HashMap<>();
        String userId = mSp_UserInfo.getString("userId", "");
        map.put("userId", userId);
        map.put("rechargeMoney", amount);
        map.put("rechargeType", type + "");
        map.put("type", "1");
        Log.i("deposit", map.get("rechargeMoney"));
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).recharge(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> respons) {
                try {
                    Gson gson = new Gson();
                    String jsonString = respons.body().string();
                    if (jsonString != null) {
                        if (type == 1) {
                            WxOrderInfoBean wxOrderInfoBean = null;
                            wxOrderInfoBean = gson.fromJson(jsonString, WxOrderInfoBean.class);
                            if (null != wxOrderInfoBean) {
                                WxOrderInfoBean.ResponseObjBean.PrepayDataBean prepayData = wxOrderInfoBean.getResponseObj().getPrepayData();
                                if (null != prepayData) {
                                    wxPay(prepayData);
                                }
                            }
                        } else {
                            ZfbOrderInfoBean zfbOrderInfoBean = null;

                            zfbOrderInfoBean = gson.fromJson(jsonString, ZfbOrderInfoBean.class);
                            if (null != zfbOrderInfoBean) {
                                zfbPay(zfbOrderInfoBean.getResponseObj().getOrderSign());
                            }
                        }
                    }
                    //  Log.i("deposit", type + "");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    /**
     * 微信支付
     */
    public void wxPay(WxOrderInfoBean.ResponseObjBean.PrepayDataBean prepayData) {
       /* Log.i("deposit",prepayData.toString());
        String appid = prepayData.getAppid();
        msgApi.registerApp(appid);*/
        PayReq request = new PayReq();
        // Log.i("deposit", prepayData.toString());
        request.appId = APP_ID + "";
        request.partnerId = prepayData.getPartnerid();
        request.prepayId = prepayData.getPrepayid();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = prepayData.getNoncestr();
        request.timeStamp = prepayData.getTimestamp();
        request.sign = prepayData.getSign();
        msgApi.sendReq(request);
    }

    /**
     * 支付宝支付
     */
    public void zfbPay(String orderinfo) {
        final String info = orderinfo;   // 订单信息

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(BalanceRechargeActivity.this);
                Map<String, String> result = alipay.payV2(info, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
    View.OnKeyListener onKey = new View.OnKeyListener() {

        @Override

        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (NetWorkUtil.isNetworkAvailable(BalanceRechargeActivity.this)) {
                    int id = mRgbWayPay.getCheckedRadioButtonId();
                    if (id == R.id.rb_zfb) {
                        recharge(id);
                    } else if (id == R.id.rb_wx) {
                        recharge(id);
                    }
                }
                return true;
            }
            return false;

        }

    };

}
