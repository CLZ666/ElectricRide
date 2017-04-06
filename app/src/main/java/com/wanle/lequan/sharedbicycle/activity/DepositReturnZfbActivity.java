package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.MessageBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
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

public class DepositReturnZfbActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.edit_input_name)
    EditText mEditInputName;
    @BindView(R.id.edit_input_account)
    EditText mEditInputAccount;
    @BindView(R.id.rgb_refund_reason)
    RadioGroup mRgbRefundReason;
    @BindView(R.id.rb_station_little)
    RadioButton mRbStationLittle;
    private SharedPreferences mSpUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_return_zfb);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("押金退款");
        mRbStationLittle.setChecked(true);
        mSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
    }

    @OnClick({R.id.iv_back, R.id.btn_confim})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confim:
                depositRefund();
                break;
        }
    }

    private void depositRefund() {
        String realName = mEditInputName.getText().toString().trim();
        String zfbAccount = mEditInputAccount.getText().toString().trim();
        final int checkedId = mRgbRefundReason.getCheckedRadioButtonId();
        String refundReason = null;
        switch (checkedId) {
            case R.id.rb_station_little:
                refundReason = "1";
                break;
            case R.id.rb_borrow_not_confident:
                refundReason = "2";
                break;
            case R.id.rb_system_not_easy_to_use:
                refundReason = "3";
                break;
            case R.id.rb_do_not_use:
                refundReason = "4";
                break;
            default:
                break;
        }
        if (TextUtils.isEmpty(realName) || TextUtils.isEmpty(zfbAccount)) {
            ToastUtils.getShortToastByString(this, "请确认所填写的信息是否正确");
        } else {
            Map map = new HashMap();
            String userId = mSpUserInfo.getString("userId", "");
            map.put("userId", userId);
            map.put("rechargeType", "2");
            map.put("realName", realName);
            map.put("alipayAccount", zfbAccount);
            map.put("clientdeviceType", "2");
            map.put("refundReason", refundReason);
            final ApiService service = HttpUtil.getService(ApiService.class);
            Call<ResponseBody> call = service.depositRefund(map);
            GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        final String jsonString = response.body().string();
                        if (null != jsonString) {
                            Gson gson = new Gson();
                            final MessageBean messageBean = gson.fromJson(jsonString, MessageBean.class);
                            if (null != messageBean) {
                                if (messageBean.getResponseCode().equals("1")) {
                                    mSpUserInfo.edit().putBoolean(getResources().getString(R.string.is_deposit), false).commit();
                                    mSpUserInfo.edit().putBoolean("isBorrow",false).commit();
                                    startActivity(new Intent(DepositReturnZfbActivity.this, MyAccountActivity.class));
                                    ToastUtil.show(DepositReturnZfbActivity.this,"退款申请已提交");
                                    finish();
                                }else {
                                    ToastUtil.show(DepositReturnZfbActivity.this,messageBean.getResponseMsg());
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
    }
}
