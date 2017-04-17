package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.FeedBackBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
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

public class FeedBackActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.edit_advice)
    EditText mEditAdvice;
    private SharedPreferences mSp_userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        mSp_userinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mTvTitle.setText("意见反馈");
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mEditAdvice.setOnKeyListener(onKey);
    }

    @OnClick({R.id.iv_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submit:
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    feedBack();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 访问网络，提交意见
     */
    public void feedBack() {
        String userId = mSp_userinfo.getString("userId", "");
        String advice = mEditAdvice.getText().toString().trim();
        if (advice.equals("")) {
            ToastUtils.getShortToastByString(this, "意见内容不能为空!");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("feedBack", advice);
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).feedBack(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    String jsonString = response.body().string();
                    FeedBackBean feedBackBean = gson.fromJson(jsonString, FeedBackBean.class);
                    if (null != feedBackBean && feedBackBean.getResponseCode().equals("1")) {
                        ToastUtils.getShortToastByString(FeedBackActivity.this, "提交成功");
                        startActivity(new Intent(FeedBackActivity.this, SettingActivity.class));
                        FeedBackActivity.this.finish();
                    } else {
                        ToastUtils.getShortToastByString(FeedBackActivity.this, "提交失败");
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
    View.OnKeyListener onKey = new View.OnKeyListener() {

        @Override

        public boolean onKey(View v, int keyCode, KeyEvent event) {



            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (NetWorkUtil.isNetworkAvailable(FeedBackActivity.this)) {
                   feedBack();
                }

                return true;

            }

            return false;

        }

    };
}
