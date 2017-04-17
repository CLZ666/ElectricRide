package com.wanle.lequan.sharedbicycle.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.TripDetailBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 充值记录页面
 */
public class TripDetailActivity extends BaseActivity implements UMShareListener {


    @BindView(R.id.tv_title)
    TextView mTvTitl;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.iv_user_icon)
    CircleImageView mIvUserIcon;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;

    @BindView(R.id.tv_start_time)
    TextView mTvStartTime;
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;
    @BindView(R.id.tv_car_no)
    TextView mTvCarNo;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_coin)
    TextView mTvCoin;
    private String mItineraryId;
    private SharedPreferences mSpUserInfo;
    private String mUserId;
    private AMap mAmp;
    private UiSettings mUiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        initView();
        getTripDetail();
        initMap();
        mMapView.onCreate(savedInstanceState);
    }

    private void initMap() {
        mAmp = mMapView.getMap();
        setMap();
    }

    /**
     * 设置AMap的一些属性
     */
    private void setMap() {
        mAmp.moveCamera(CameraUpdateFactory.zoomTo(16));
        mUiSettings = mAmp.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
    }

    private void getTripDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", mUserId);
        map.put("itineraryId", mItineraryId);
        Log.i("ID", mItineraryId);
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).getTripDetail(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    if (jsonString != null) {
                        Gson gson = new Gson();
                        TripDetailBean tripDetailBean = gson.fromJson(jsonString, TripDetailBean.class);
                        if (null != tripDetailBean) {
                            if (tripDetailBean.getResponseCode().equals("1")) {
                                if (null != tripDetailBean.getResponseObj().getItinerary()) {
                                    routeLine(tripDetailBean);
                                    setDeatail(tripDetailBean);
                                }

                            } else {
                                ToastUtil.show(TripDetailActivity.this, tripDetailBean.getResponseMsg());
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

    private void setDeatail(TripDetailBean tripDetailBean) {
        int cycleTime = tripDetailBean.getResponseObj().getItinerary().getCycleTime();
        mTvTime.setText(cycleTime + " 分钟");
        int cycleCharge = tripDetailBean.getResponseObj().getItinerary().getCycleCharge();
        double charge = cycleCharge * 1.0 / 100;
        mTvCoin.setText(charge + " 元");
        long startTime = tripDetailBean.getResponseObj().getItinerary().getStartTime();
        mTvStartTime.setText(stampToDate(startTime + ""));
        long endTime = tripDetailBean.getResponseObj().getItinerary().getEndTime();
        mTvEndTime.setText(stampToDate(endTime + ""));
        String carNo = tripDetailBean.getResponseObj().getItinerary().getCarNo();
        mTvCarNo.setText(carNo);
    }

    /*
       * 将时间戳转换为时间
       */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = Long.valueOf(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 在起点和终点间划线
     *
     * @param tripDetailBean
     */
    private void routeLine(TripDetailBean tripDetailBean) {
        List<TripDetailBean.ResponseObjBean.ItineraryRecordBean> itineraryRecord = tripDetailBean.getResponseObj().getItineraryRecord();
        if (null != itineraryRecord) {
            TripDetailBean.ResponseObjBean.ItineraryRecordBean startPoint = tripDetailBean.getResponseObj().getItineraryRecord().get(0);
            TripDetailBean.ResponseObjBean.ItineraryRecordBean endPoint = tripDetailBean.getResponseObj().getItineraryRecord().get(1);
            LatLng startLng = new LatLng(Double.parseDouble(startPoint.getLatitude()), Double.parseDouble(startPoint.getLongitude()));
            LatLng endLng = new LatLng(Double.parseDouble(endPoint.getLatitude()), Double.parseDouble(endPoint.getLongitude()));
            Log.i("latlng", startLng.toString());
            mAmp.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_start))
                    .position(startLng));
            mAmp.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_end))
                    .position(endLng));
            ArrayList<LatLng> latlngs = new ArrayList<>();
            latlngs.add(startLng);
            latlngs.add(endLng);
            Polyline polyline = mAmp.addPolyline(new PolylineOptions()
                    .addAll(latlngs));
            polyline.setColor(getResources().getColor(R.color.red));
            polyline.setWidth(14.0f);
            mAmp.moveCamera(CameraUpdateFactory.newLatLngZoom(startLng, 18));
        }

    }


    private void initView() {
        mTvTitl.setText("行程明细");
        mTvTitl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        String itineraryId = getIntent().getStringExtra("itineraryId");
        if (itineraryId != null) {
            mItineraryId = itineraryId;
        } else {
            mItineraryId = "bc80e9fcf7e411e6b1218e34576bcc2e";
        }
        mSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mUserId = mSpUserInfo.getString("userId", "");
        String headimg = mSpUserInfo.getString("headimg", "");
        Glide.with(this).load(headimg).asBitmap().into(mIvUserIcon);
        String phone = mSpUserInfo.getString("phone", "");
        mTvPhone.setText(phone);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @OnClick({R.id.iv_back, R.id.btn_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_share:
                shareOption();
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

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        ToastUtil.show(TripDetailActivity.this, "分享成功");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        ToastUtil.show(TripDetailActivity.this, "分享失败");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        ToastUtil.show(TripDetailActivity.this, "分享取消");
    }
}
