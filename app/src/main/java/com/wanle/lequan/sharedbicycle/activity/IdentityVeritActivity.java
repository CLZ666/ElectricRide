package com.wanle.lequan.sharedbicycle.activity;

        import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.IdentityVerifyBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * 实名认证界面
 */
public class IdentityVeritActivity extends BaseActivity {
    @BindView(R.id.edit_name)
    EditText mEditName;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    private SharedPreferences mSp_identity;
    private SharedPreferences.Editor mEdit_identity;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.line_depositrecharge)
    TextView mLineDepositrecharge;
    @BindView(R.id.tv_deposit)
    TextView mTvDeposit;
    @BindView(R.id.line_certification)
    TextView mLineCertification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_verit);
        ButterKnife.bind(this);
        mLineDepositrecharge.setBackgroundColor(getResources().getColor(R.color.red));
        mLineCertification.setBackgroundColor(getResources().getColor(R.color.red));
        mTvTitle.setText("实名认证");
        mTvDeposit.setTextColor(getResources().getColor(R.color.red));
        mTvDeposit.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.complete_verify), null, null);
        mSp_identity = getSharedPreferences("userinfo", MODE_PRIVATE);
        mEdit_identity = mSp_identity.edit();
        mEditCode.setOnKeyListener(onKey);

        //getJsonString();
    }

    private void getJsonString() {
        String name = mEditName.getText().toString().trim();
        String code = mEditCode.getText().toString().trim();
        if (name.equals("") || code.equals("")) {
            ToastUtils.getShortToastByString(this, "请确认信息是否填写正确");
        } else {

            Map<String, String> map = new HashMap<>();
            String userId = getSharedPreferences("userinfo", MODE_PRIVATE).getString("userId", "");
            //map.put("userId","99DBD039C7DCE2DA86889143946687EF6BD790241761D8CD8147EA299742DBCD6B2DC180FD294EC25F7DEBEB1F2B0DA7");
            map.put("userId", userId);
            map.put("userName", name);
            map.put("certificateNo", code);
            Call<ResponseBody> call = HttpUtil.getService(ApiService.class).getIdentifyString(map);
            // GetJsonStringUtil.getJson_String(call);
        }
    }

    /**
     * 访问实名认证接口
     */
    private void IdentityVerify() {
        String name = mEditName.getText().toString().trim();
        String code = mEditCode.getText().toString().trim();
        if (name.equals("") || code.equals("")) {
            ToastUtils.getShortToastByString(this, "请确认信息是否填写正确");
        } else {

            Map<String, String> map = new HashMap<>();
            String userId = getSharedPreferences("userinfo", MODE_PRIVATE).getString("userId", "");
            //map.put("userId","99DBD039C7DCE2DA86889143946687EF6BD790241761D8CD8147EA299742DBCD6B2DC180FD294EC25F7DEBEB1F2B0DA7");
            map.put("userId", userId);
            map.put("userName", name);
            map.put("certificateNo", code);
            Observable<IdentityVerifyBean> observable = HttpUtil.getService(ApiService.class).identity_verify(map);
            HttpUtil.init(observable, new Subscriber<IdentityVerifyBean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                        /*ToastUtils.getShortToastByString(IdentityVeritActivity.this,"实名认证失败");
                        Log.i("identify",e.getMessage());*/
                }

                @Override
                public void onNext(IdentityVerifyBean identityVerifyBean) {
                    Log.i("identify", identityVerifyBean.toString());
                    String responseMsg = identityVerifyBean.getResponseMsg();
                    if (identityVerifyBean.getResponseCode().equals("1")) {
                        mEdit_identity.putBoolean(getResources().getString(R.string.is_identity), true).commit();
                        ToastUtils.getShortToastByString(IdentityVeritActivity.this, "实名认证成功");
                        startActivity(new Intent(IdentityVeritActivity.this, UserInfoActivity.class));
                        finish();
                    } else {
                        ToastUtils.getShortToastByString(IdentityVeritActivity.this, responseMsg);
                    }
                }
            });
        }

    }

    @OnClick({R.id.iv_back, R.id.btn_confim})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confim:
                IdentityVerify();
                //getJsonString();
                break;
            default:
                break;
        }
    }

    View.OnKeyListener onKey = new View.OnKeyListener() {

        @Override

        public boolean onKey(View v, int keyCode, KeyEvent event) {


            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (NetWorkUtil.isNetworkAvailable(IdentityVeritActivity.this)) {
                    IdentityVerify();
                }
                return true;
            }

            return false;

        }

    };

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
