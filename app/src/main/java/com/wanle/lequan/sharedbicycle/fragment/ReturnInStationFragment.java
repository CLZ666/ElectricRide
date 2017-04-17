package com.wanle.lequan.sharedbicycle.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.ReturnCheckBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReturnInStationFragment extends DialogFragment {


    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.iv_cancel)
    ImageView mIvCancel;
    @BindView(R.id.tv_station_address)
    TextView mTvStationAddress;
    private AMap mAmp;
    private UiSettings mUiSettings;
    private LatLng mLocatePoint;
    private ReturnCheckBean.ResponseObjBean.PlaceInBean mPlaceInBean;

    public static ReturnInStationFragment newInstance(LatLng locatePoint, ReturnCheckBean.ResponseObjBean.PlaceInBean  placeInBean) {

        Bundle args = new Bundle();
        args.putParcelable("locatePoint", locatePoint);
        args.putParcelable("PlaceInBean", placeInBean);
        ReturnInStationFragment fragment = new ReturnInStationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);//无法直接点击外部取消dialog
       // setStyle(DialogFragment.STYLE_NO_FRAME,0); //NO_FRAME就是dialog无边框，0指的是默认系统Theme
        final Bundle args = getArguments();
        mLocatePoint = args.getParcelable("locatePoint");
        mPlaceInBean = args.getParcelable("PlaceInBean");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_return_in_station, container, false);
        ButterKnife.bind(this, view);
        mMapView.onCreate(savedInstanceState);
        initView();
        initMap();
        paintInAare();
        return view;
    }

    private void initView() {
        if (null!=mPlaceInBean){
            mTvStationAddress.setText(mPlaceInBean.getPlaceName());
        }
    }

    private void initMap() {
        mAmp = mMapView.getMap();
        setMap();
    }

    /**
     * 根据服务器传来的几个点，画还车区域和定位点
     */
    public void paintInAare() {
        mAmp.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(mLocatePoint)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.small_end_point)));
        ArrayList<LatLng> points = new ArrayList<>();
        if (null!=mPlaceInBean){
            for (ReturnCheckBean.ResponseObjBean.PlaceInBean.LongLatiJsonBean latlng : mPlaceInBean.getLongLatiJson()) {
                LatLng point = new LatLng(Double.parseDouble(latlng.getLatitude()), Double.parseDouble(latlng.getLongitude()));
                points.add(point);
            }
        }else {
            return;
        }
        // 声明 多边形参数对象
        PolygonOptions polygonOptions = new PolygonOptions();
        // 添加 多边形的每个顶点（顺序添加）
        for (int i = 0; i < points.size(); i++) {
            polygonOptions.add(points.get(i));
        }
        polygonOptions.strokeWidth(15) // 多边形的边框
                .strokeColor(Color.argb(50, 1, 1, 1)) // 边框颜色
                .fillColor(Color.argb(1, 1, 1, 1));   // 多边形的填充色
    }

    /**
     * 设置AMap的一些属性
     */
    private void setMap() {
        //mAmp.moveCamera(CameraUpdateFactory.zoomTo(16));
        mUiSettings = mAmp.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
    }

    @OnClick({R.id.iv_cancel, R.id.btn_confim_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel:
                dismiss();
                break;
            case R.id.btn_confim_return:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

}
