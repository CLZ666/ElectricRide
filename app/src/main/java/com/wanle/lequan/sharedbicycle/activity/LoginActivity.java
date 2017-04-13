package com.wanle.lequan.sharedbicycle.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.wanle.lequan.sharedbicycle.MainActivity;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.UserInfoBean;
import com.wanle.lequan.sharedbicycle.bean.VerificationCode;
import com.wanle.lequan.sharedbicycle.bean.loginBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.CountDownTimerUtils;
import com.wanle.lequan.sharedbicycle.utils.GetUserInfo;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;
import com.wanle.lequan.sharedbicycle.view.ProgersssDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private CountDownTimerUtils mCountDownUtils;
    private SharedPreferences mSp_userinfo;
    private SharedPreferences.Editor mEdit_userinfo;
    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.edit_identify)
    EditText mEditIdentify;
    @BindView(R.id.btn_get_code)
    Button mBtnGetCode;
    private ProgersssDialog mProgersssDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mEditPhone.addTextChangedListener(textWatcher);
        mSp_userinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mEdit_userinfo = mSp_userinfo.edit();
        showKeyboard(mEditPhone);
        showKeyboard(mEditIdentify);
        mEditPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        mEditIdentify.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        mEditIdentify.setOnKeyListener(onKey);
    }


    @OnClick({R.id.iv_back, R.id.btn_get_code, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_get_code:
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    getCode();
                }
                break;
            case R.id.btn_login:
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    login();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 注册登录
     */
    private void login() {
        String phone = mEditPhone.getText().toString().trim();
        String code = mEditIdentify.getText().toString().trim();
        if (phone.equals("") || code.equals("") || !isMobileNO(phone)) {
            ToastUtils.getShortToastByString(this, "请确认填写的信息是否正确");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("smsCode", code);
            Observable<loginBean> observable = HttpUtil.getService(ApiService.class).getLoginInfo(map);
            HttpUtil.init(observable, new Subscriber<loginBean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e.getMessage() != null) {
                        ToastUtils.getShortToastByString(LoginActivity.this, "验证码错误或者已过期!");
                    }
                }

                @Override
                public void onNext(loginBean loginBean) {
                    if (loginBean != null) {
                        Log.i("888", loginBean.toString());
                        String responseCode = loginBean.getResponseCode();
                        if (responseCode.equals("1")) {
                            mProgersssDialog = new ProgersssDialog(LoginActivity.this);
                            mProgersssDialog.setMsg("正在登录中");
                            mEdit_userinfo.putString("userId", loginBean.getResponseObj().getUserId());
                            mEdit_userinfo.commit();
                            getSharedPreferences("isLogin", MODE_PRIVATE).edit().putBoolean("isLogin", true).commit();
                            getUserInfo();
                        }else{
                            ToastUtil.show(LoginActivity.this,loginBean.getResponseMsg());
                        }
                    }
                }
            });
        }
    }

    /**
     * 访问网络获得用户信息
     */
    public void getUserInfo() {
        GetUserInfo.getUserInfo(LoginActivity.this, new Subscriber<UserInfoBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UserInfoBean userInfoBean) {
                if (null!=userInfoBean&&userInfoBean.getResponseCode().equals("1")){
                    String headImg = userInfoBean.getResponseObj().getHeadImg();
                    if (!headImg.equals("")){
                        mEdit_userinfo.putString("headimg", headImg).commit();
                    }
                    String userName = userInfoBean.getResponseObj().getUserName();
                    mEdit_userinfo.putString("userName", userName).commit();
                    int balance1 = userInfoBean.getResponseObj().getBalance();
                    double balance = balance1 * 1.0 / 100;
                    mEdit_userinfo.putString("balance", balance + "");
                    String phone = userInfoBean.getResponseObj().getPhone();
                    mEdit_userinfo.putString("phone", phone).commit();
                    int payDeposit = userInfoBean.getResponseObj().getPayDeposit();
                    int isVerified = userInfoBean.getResponseObj().getIsVerified();
                    if (isVerified == 1 && payDeposit > 0) {
                        mProgersssDialog.dismiss();
                        mEdit_userinfo.putBoolean("isBorrow", true);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        mProgersssDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, DepositActivity.class));
                    }

                    if (payDeposit > 0) {
                        mEdit_userinfo.putBoolean(getResources().getString(R.string.is_deposit), true).commit();
                    }
                    if (isVerified == 1) {
                        mEdit_userinfo.putBoolean(getResources().getString(R.string.is_identity), true).commit();
                    }
                    Log.i("888", userInfoBean.toString());
                    finish();
                }
            }
        });
    }

    //访问网络，获得手机验证码
    private void getCode() {
        String phone = mEditPhone.getText().toString().trim();
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();
        ApiService service = retrofit.create(ApiService.class);
        if (isMobileNO(phone)) {
            Observable<VerificationCode> observable = service.getVerificationCode(phone);
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<VerificationCode>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(VerificationCode verificationCode) {
                            if (verificationCode != null) {
                                boolean isSSuccess = verificationCode.getResponseCode().equals("1");
                                if (isSSuccess) {
                                    ToastUtils.getShortToastByString(LoginActivity.this, "验证码已发送,请注意查收");
                                    mCountDownUtils = new CountDownTimerUtils(LoginActivity.this, mBtnGetCode, 60000, 1000);
                                    mCountDownUtils.start();
                                    mBtnGetCode.setTextColor(getResources().getColor(R.color.colorAccent));
                                } else {
                                    ToastUtils.getShortToastByString(LoginActivity.this, verificationCode.getResponseMsg());
                                }
                            }

                        }
                    });
        } else {
            ToastUtils.getShortToastByString(this, "手机号格式不正确");
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    //对edittext输内容时进行监听
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phone = mEditPhone.getText().toString().trim();
            if (isMobileNO(phone)) {
                mBtnGetCode.setTextColor(getResources().getColor(R.color.red));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String phone = mEditPhone.getText().toString().trim();
            if (!isMobileNO(phone)) {
                mBtnGetCode.setTextColor(getResources().getColor(R.color.white));
            }
        }
    };
    View.OnKeyListener onKey = new View.OnKeyListener() {

        @Override

        public boolean onKey(View v, int keyCode, KeyEvent event) {

// TODO Auto-generated method stub

            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (NetWorkUtil.isNetworkAvailable(LoginActivity.this)) {
                    login();
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
