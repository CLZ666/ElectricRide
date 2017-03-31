package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wanle.lequan.sharedbicycle.MainActivity;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.PayResult;
import com.wanle.lequan.sharedbicycle.bean.WxOrderInfoBean;
import com.wanle.lequan.sharedbicycle.bean.ZfbOrderInfoBean;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 押金充值页面
 */
public class DepositActivity extends AppCompatActivity {
    private static final int SDK_PAY_FLAG = 1;
    private SharedPreferences mSp_borrow;
    private SharedPreferences.Editor mEdit_borrow;
    private static final String APP_ID = "wx1902de5450f7a61e";
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, APP_ID, false);
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult result = new PayResult((Map<String, String>) msg.obj);
            String memo = result.getMemo();
            Log.i("deposit", memo);
            if (memo.equals("操作已经取消。")) {
                ToastUtils.getShortToastByString(DepositActivity.this, "操作已经取消");
            } else if (memo.equals("")) {
                ToastUtils.getShortToastByString(DepositActivity.this, "支付成功");
                mEdit_borrow.putBoolean(getResources().getString(R.string.is_deposit), true).commit();
                boolean isIdentify = mSp_borrow.getBoolean(getResources().getString(R.string.is_identity), false);
                if (!isIdentify) {
                    startActivity(new Intent(DepositActivity.this, IdentityVeritActivity.class));
                } else {
                    startActivity(new Intent(DepositActivity.this, MainActivity.class));
                }
                DepositActivity.this.finish();
            }
        }

        ;
    };
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rb_zfb)
    RadioButton mRbZfb;
    @BindView(R.id.rb_wx)
    RadioButton mRbWx;
    @BindView(R.id.rgb_way_pay)
    RadioGroup mRgbWayPay;
    @BindView(R.id.line_depositrecharge)
    TextView mLineDepositrecharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        ButterKnife.bind(this);
        mTvTitle.setText("押金充值");
        mLineDepositrecharge.setBackgroundColor(getResources().getColor(R.color.red));
        mRbZfb.setChecked(true);
        mSp_borrow = getSharedPreferences("userinfo", MODE_PRIVATE);
        mEdit_borrow = mSp_borrow.edit();
    }

    @OnClick({R.id.rb_zfb, R.id.rb_wx, R.id.btn_confim, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confim:
                int id = mRgbWayPay.getCheckedRadioButtonId();
                if (id == R.id.rb_zfb) {
                    recharge(id);
                } else if (id == R.id.rb_wx) {
                    recharge(id);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 押金充值
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
        Map<String, String> map = new HashMap<>();
        String userId = mSp_borrow.getString("userId", "");
        map.put("userId", userId);
        map.put("rechargeMoney", "1");
        map.put("rechargeType", type + "");
        map.put("type", "2");
        Log.i("deposit", map.get("rechargeMoney"));
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).recharge(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> respons) {
                Gson gson = new Gson();
                String jsonString = null;
                try {
                    jsonString = respons.body().string();
                    if (null != jsonString) {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("deposit", type + "");
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
        Log.i("deposit", prepayData.toString());
        request.appId = prepayData.getAppid();
        request.partnerId = prepayData.getPartnerid();
        request.prepayId = prepayData.getPrepayid();
        request.packageValue = prepayData.getPackageX();
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
                PayTask alipay = new PayTask(DepositActivity.this);
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

}
