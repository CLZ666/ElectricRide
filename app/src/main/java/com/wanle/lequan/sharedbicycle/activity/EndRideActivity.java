package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.EndRideBean;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EndRideActivity extends AppCompatActivity implements UMShareListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_ride_cost)
    TextView mTvRideCost;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.tv_ride_time)
    TextView mTvRideTime;
    private EndRideBean mEndRideBean;
    private SharedPreferences mSpUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_ride);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("骑行结束");
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mEndRideBean = getIntent().getParcelableExtra("endRide");
        mSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);

        if (null != mEndRideBean) {
            final int balance = mEndRideBean.getResponseObj().getBalance();
            double banlce = balance * 1.0 / 100;
            mTvBalance.setText(banlce + "");
            mTvRideCost.setText("支付: ￥ " + mEndRideBean.getResponseObj().getCycleCharge() / 100);
            int time = mEndRideBean.getResponseObj().getTimeTotal();
            mTvRideTime.setText(getStandardTime(time));
            mSpUserInfo.edit().putString("balance",mEndRideBean.getResponseObj().getBalance()*1.0/100+"").commit();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_trip_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_trip_detail:
                startActivity(new Intent(this, TripDetailActivity.class).putExtra("itineraryId", mEndRideBean.getResponseObj().getItineraryId()));
                finish();
                break;
            default:
                break;
        }
    }

    private void shareOption() {
        UMImage image = new UMImage(this, "http://img.lequangroup.cn/Categroy/48f493ad138d400b91b17c0a1f941060_750x300.png");
        UMImage thumb = new UMImage(this, R.drawable.logo);
        image.setThumb(thumb);
        image.compressStyle = UMImage.CompressStyle.SCALE;
        UMWeb web = new UMWeb("http://h5.lequangroup.cn/");
        web.setTitle("乐圈国际馆");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("精选全球好货");//描述
        new ShareAction(this).withText("hello")
                .withMedia(image)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(this).open();
    }

    public String getStandardTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = new Date(timestamp * 1000 * 60);
        sdf.format(date);
        return sdf.format(date);
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        ToastUtil.show(this, "分享成功");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        ToastUtil.show(this, "分享失败");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        ToastUtil.show(this, "分享取消");
    }
}
