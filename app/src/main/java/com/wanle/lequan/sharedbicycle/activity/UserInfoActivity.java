package com.wanle.lequan.sharedbicycle.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.UserInfoBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.Subscriber;

public class UserInfoActivity extends AppCompatActivity implements UMShareListener {


    @BindView(R.id.tv_phone_verification)
    TextView mTvPhoneVerification;
    @BindView(R.id.line_depositrecharge)
    TextView mLineDepositrecharge;
    @BindView(R.id.line_certification)
    TextView mLineCertification;
    @BindView(R.id.line_startborrow)
    TextView mLineStartborrow;
    @BindView(R.id.tv_deposit)
    TextView mTvDeposit;
    @BindView(R.id.tv_identify)
    TextView mTvIdentify;
    @BindView(R.id.tv_setting)
    TextView mTvSetting;
    @BindView(R.id.linear_borrow)
    LinearLayout mLinearBorrow;
    @BindView(R.id.tv_username)
    TextView mTvUsername;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.user_icon)
    CircleImageView mUserIcon;

    private boolean mIsDeposit;
    private boolean mIsIdentify;
    private UserInfoBean mUserInfo;
    private ImageView mIvBack;
    private SharedPreferences mSpUserInfo;
    private boolean mIsBorrow;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_SHARE_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        getUserInfo();
        UpdateUi();
    }

    private void getUserInfoString() {
        String userId = getSharedPreferences("userinfo", MODE_PRIVATE).getString("userId", "");
        // String userId = "9DBD039C7DCE2DA86889143946687EF6BD790241761D8CD8147EA299742DBCD6B2DC180FD294EC25F7DEBEB1F2B0DA7";
        Log.i("userInfo", userId);
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);

    }

    private void UpdateUi() {
        String headimg = mSpUserInfo.getString("headimg", "");
        Glide.with(UserInfoActivity.this).load(headimg).placeholder(getResources().getDrawable(R.drawable.userhead)).into(mUserIcon);
        String balance = mSpUserInfo.getString("balance", "");
        mTvBalance.setText("￥" + balance);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIsBorrow = mSpUserInfo.getBoolean("isBorrow", false);
        if (mIsBorrow) {
            mLinearBorrow.setVisibility(View.GONE);
        }
        //getUserInfoString();
        String username = mSpUserInfo.getString("userName", "");
        mTvUsername.setText(username);
        //判断押金是否已充值
        mIsDeposit = mSpUserInfo
                .getBoolean(getResources().getString(R.string.is_deposit), false);
        //判断是否已经实名认证
        mIsIdentify = mSpUserInfo
                .getBoolean(getResources().getString(R.string.is_identity), false);
        if (mIsDeposit) {
            mLineDepositrecharge.setBackgroundColor(getResources().getColor(R.color.red));
            mTvDeposit.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.complete_verify), null, null);
            mTvDeposit.setTextColor(getResources().getColor(R.color.red));
        }
        if (mIsDeposit && mIsIdentify) {
            mLineCertification.setBackgroundColor(getResources().getColor(R.color.red));
            mTvIdentify.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.complete_verify), null, null);
            mTvIdentify.setTextColor(getResources().getColor(R.color.red));
        }
    }

    /**
     * 访问网络获得用户的个人信息
     */
    private void getUserInfo() {
        final String userId = mSpUserInfo.getString("userId", "");

        // String userId = "99DBD039C7DCE2DA86889143946687EF6BD790241761D8CD8147EA299742DBCD6B2DC180FD294EC25F7DEBEB1F2B0DA7";
        Log.i("userinfo", userId);
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        Observable<UserInfoBean> observable = HttpUtil.getService(ApiService.class).getUserInfo(map);
        HttpUtil.init(observable, new Subscriber<UserInfoBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UserInfoBean userInfoBean) {
                //Log.i("888", userInfoBean.toString());
                if (userInfoBean != null && userInfoBean.getResponseCode().equals("1")) {
                    String headImg = userInfoBean.getResponseObj().getHeadImg();
                    mSpUserInfo.edit().putString("headimg", headImg).commit();
                    Glide.with(UserInfoActivity.this).load(headImg).into(mUserIcon);
                    String userName = userInfoBean.getResponseObj().getUserName();
                    mSpUserInfo.edit().putString("userName", userName).commit();
                    int balance1 = userInfoBean.getResponseObj().getBalance();
                    double balance = balance1 * 1.0 / 100;
                    mSpUserInfo.edit().putString("balance", balance + "").commit();
                    String phone = userInfoBean.getResponseObj().getPhone();
                    mSpUserInfo.edit().putString("phone", phone).commit();
                    //  String phone = userInfoBean.getResponseObj().getPhone();
                    mTvUsername.setText(userName);
                    int payDeposit = userInfoBean.getResponseObj().getPayDeposit();
                    int isVerified = userInfoBean.getResponseObj().getIsVerified();
                    if (payDeposit > 0) {
                        mSpUserInfo.edit().putBoolean(getResources().getString(R.string.is_deposit), true).commit();
                    }
                    if (isVerified == 1) {
                        mSpUserInfo.edit().putBoolean(getResources().getString(R.string.is_identity), true).commit();
                    }
                    if (payDeposit > 0 && isVerified == 1) {
                        mSpUserInfo.edit().putBoolean("isBorrow", true).commit();
                    }
                    Log.i("888", userInfoBean.toString());
                }
            }

        });
    }

    @OnClick({R.id.linear_borrow, R.id.rel_my_account, R.id.rel_my_trip, R.id.rel_my_news, R.id.rel_my_invite, R.id.rel_user_guide, R.id.rel_my_contact_us, R.id.iv_back, R.id.tv_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_borrow:
                if (!mIsDeposit) {
                    startActivity(new Intent(this, DepositActivity.class));
                } else if (!mIsIdentify) {
                    startActivity(new Intent(this, IdentityVeritActivity.class));
                } else {
                    mSpUserInfo.edit().putBoolean("isBorrow", true).commit();
                    finish();
                }
                break;
            case R.id.rel_my_account:
                if (mIsDeposit) {
                    startActivity(new Intent(this, MyAccountActivity.class));
                } else {
                    ToastUtils.getShortToastByString(this, "请先充值押金");
                }
                break;
            case R.id.rel_my_trip:
                startActivity(new Intent(this, MyTripActivity.class));
                break;
            case R.id.rel_my_news:
                startActivity(new Intent(this, MyNewsActivity.class));
                break;
            case R.id.rel_my_invite:
                //SharePermission();
                shareOption();
                break;
            case R.id.rel_user_guide:
                startActivity(new Intent(this, UserGuideActivity.class));
                break;
            case R.id.rel_my_contact_us:
                testCall();
                break;
            case R.id.tv_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    private void shareOption() {
        UMImage image = new UMImage(this, "http://img.lequangroup.cn/Categroy/48f493ad138d400b91b17c0a1f941060_750x300.png");
        UMImage thumb = new UMImage(this, R.mipmap.ic_launcher);
        image.setThumb(thumb);
        image.compressStyle = UMImage.CompressStyle.SCALE;
        UMWeb web = new UMWeb("http://www.ifeng.com/");
        web.setTitle("This is music title");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("my description");//描述
        new ShareAction(this).withText("hello")
                .withMedia(image)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(this).open();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUserInfo();
        UpdateUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        UpdateUi();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void testCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            callPhone();
        }
    }

    public void callPhone() {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "18855091326"));//跳转到拨号界面，同时传递电话号码
        startActivity(dialIntent);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else {
                ToastUtils.getShortToastByString(this, "权限已被拒绝");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (!Settings.canDrawOverlays(this)) {
                // SYSTEM_ALERT_WINDOW permission not granted...
                Toast.makeText(this, "not granted", Toast.LENGTH_SHORT);
            }
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        ToastUtil.show(UserInfoActivity.this, "分享成功");

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        ToastUtil.show(UserInfoActivity.this, "分享失败");
        Log.i("share1", throwable.getMessage());
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        ToastUtil.show(UserInfoActivity.this, "分享取消");
    }


}
