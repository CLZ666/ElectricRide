package com.wanle.lequan.sharedbicycle.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.activity.EndRideActivity;
import com.wanle.lequan.sharedbicycle.bean.EndRideBean;
import com.wanle.lequan.sharedbicycle.bean.ReturnCheckBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReturnInStationFragment extends DialogFragment {


    @BindView(R.id.map)
    TextureMapView mMapView;
    @BindView(R.id.iv_cancel)
    ImageView mIvCancel;
    @BindView(R.id.tv_station_address)
    TextView mTvStationAddress;
    private AMap mAmp;
    private UiSettings mUiSettings;
    private LatLng mLocatePoint;
    private Context mContext;
    private ReturnCheckBean.ResponseObjBean.PlaceInBean mPlaceInBean;

    public static ReturnInStationFragment newInstance(LatLng locatePoint, ReturnCheckBean.ResponseObjBean.PlaceInBean placeInBean) {

        Bundle args = new Bundle();
        args.putParcelable("locatePoint", locatePoint);
        args.putParcelable("PlaceInBean", placeInBean);
        ReturnInStationFragment fragment = new ReturnInStationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
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
        setCancelable(true);//无法直接点击外部取消dialog
        this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white2)));
        cancelOutSide();
        initMap();
        paintInAare();
        return view;
    }

    /**
     * 点击对话框外部，对话框消失
     */
    public void cancelOutSide() {
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    ReturnInStationFragment.this.dismiss();
                    return true; // pretend we've processed it
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }

    private void initView() {
        if (null != mPlaceInBean) {
            mTvStationAddress.setText(mPlaceInBean.getPlaceName());
        }
    }

    private void initMap() {
        if (mAmp == null) {
            mAmp = mMapView.getMap();
        }
        setMap();
    }

    /**
     * 还车操作
     *
     * @param isInStation
     */
    public void returnCar(int isInStation) {
        String userId = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE).getString("userId", "");
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        final double longitude = mLocatePoint.longitude;
        final double latitude = mLocatePoint.latitude;
        map.put("longitude", longitude + "");
        map.put("latitude", latitude + "");
        map.put("isInStation", isInStation + "");
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).returnCar(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    Log.i("returncar", jsonString);
                    if (null != jsonString) {
                        Gson gson = new Gson();
                        EndRideBean endRideBean = gson.fromJson(jsonString, EndRideBean.class);
                        if (null != endRideBean) {
                            if (endRideBean.getResponseCode().equals("1")) {
                                Intent intent = new Intent(mContext, EndRideActivity.class);
                                intent.putExtra("endRide", endRideBean);
                                mContext.startActivity(intent);
                            } else {
                                ToastUtil.show(getActivity(), endRideBean.getResponseMsg());
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

    /**
     * 根据服务器传来的几个点，画还车区域和定位点
     */
    public void paintInAare() {
        mAmp.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(mLocatePoint)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_point)));
        ArrayList<LatLng> points = new ArrayList<>();
        if (null != mPlaceInBean) {
            for (ReturnCheckBean.ResponseObjBean.PlaceInBean.LongLatiJsonBean latlng : mPlaceInBean.getLongLatiJson()) {
                LatLng point = new LatLng(Double.parseDouble(latlng.getLatitude()), Double.parseDouble(latlng.getLongitude()));
                points.add(point);
            }
        } else {
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
        mAmp.addPolygon(polygonOptions);
        // mAmp.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocatePoint,8));
        zoomToSpan(points);
    }

    /**
     * 移动镜头到当前的视角。
     *
     * @since V2.1.0
     */
    public void zoomToSpan(ArrayList<LatLng> points) {

        if (mAmp == null)
            return;
        try {
            LatLngBounds bounds = getLatLngBounds(points);
            mAmp.animateCamera(CameraUpdateFactory
                    .newLatLngBounds(bounds, 30));
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    protected LatLngBounds getLatLngBounds(ArrayList<LatLng> points) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < points.size(); i++) {
            b.include(points.get(i));
        }
        return b.build();
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
                if (NetWorkUtil.isNetworkAvailable(getActivity())) {
                    returnCar(1);
                    dismiss();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
