package com.wanle.lequan.sharedbicycle;

import android.Manifest;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.activity.BlueToothActivity;
import com.wanle.lequan.sharedbicycle.activity.DepositActivity;
import com.wanle.lequan.sharedbicycle.activity.EndRideActivity;
import com.wanle.lequan.sharedbicycle.activity.IdentityVeritActivity;
import com.wanle.lequan.sharedbicycle.activity.IsLoginActivity;
import com.wanle.lequan.sharedbicycle.activity.LoginActivity;
import com.wanle.lequan.sharedbicycle.activity.SearchActivity;
import com.wanle.lequan.sharedbicycle.activity.SweepLockActivity;
import com.wanle.lequan.sharedbicycle.activity.UserInfoActivity;
import com.wanle.lequan.sharedbicycle.adapter.InfoWinAdapter;
import com.wanle.lequan.sharedbicycle.bean.AddressInfo;
import com.wanle.lequan.sharedbicycle.bean.CarState;
import com.wanle.lequan.sharedbicycle.bean.CarStateBean;
import com.wanle.lequan.sharedbicycle.bean.MessageBean;
import com.wanle.lequan.sharedbicycle.bean.NearByCarBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.event.CarStateEvent;
import com.wanle.lequan.sharedbicycle.event.MyEvent;
import com.wanle.lequan.sharedbicycle.fragment.AddressInfoFragment;
import com.wanle.lequan.sharedbicycle.fragment.CarStateFragment;
import com.wanle.lequan.sharedbicycle.overlay.WalkRouteOverlay;
import com.wanle.lequan.sharedbicycle.receiver.BlueToothStateReceiver;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;
import com.wanle.lequan.sharedbicycle.view.ProgersssDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, LocationSource, AMapLocationListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener, RouteSearch.OnRouteSearchListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener {
    @BindView(R.id.map)
    MapView mMap;
    @BindView(R.id.btn_use_car)
    Button mBtnUseCar;
    @BindView(R.id.iv_end_point)
    ImageView mIvEndPoint;
    @BindView(R.id.iv_bike_station)
    ImageView mIvBikeStation;
    private SharedPreferences mSp_isLogin;
    private AMap aMap;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    OnLocationChangedListener mListener;
    //声明AMapLocationClient类对象
    public AMapLocationClient mlocationClient = null;
    AMapLocationClientOption mLocationOption;
    private LatLng mLocalLatlng;
    private Marker mCenterMarker;
    AMapLocation mAmapocation;
    GeocodeSearch mGeocodeSearch;
    private LatLng mCenterPoint;
    RouteSearch mRouteSearch;
    private RouteSearch.WalkRouteQuery mQueryWalk;
    WalkRouteResult mWalkRouteResult;
    private WalkRouteOverlay mWalkRouteOverlay;
    private ProgersssDialog progDialog = null;// 规划路径时的进度条
    private SharedPreferences mSpUserinfo;
    private static final int REQUEST_CODE = 1;
    private static final int RESULT_CODE = 2;

    private AddressInfoFragment mAddressInfoFragment;
    private FragmentManager mFm;
    private FragmentTransaction mTransaction;
    private ProgersssDialog mProgersssDialog;
    private static final int MY_PERMISSIONS_REQUEST_MAP = 1;
    private static final int MY_PERMISSIONS_REQUEST_QR_CODE = 2;
    private int mCar_amount;
    private Handler carStateHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            isUserCar();
            carStateHandler.postDelayed(this, 10000);
        }
    };
    private CarStateFragment mCarStateFragment = new CarStateFragment();
    private boolean mIsStation;
    private InfoWinAdapter mInfoWinAdapter;
    private Marker mOldMarker;
    private BlueToothStateReceiver mBlueToothStateReceiver;
    private CountDownTimer mCdt;
    private CountDownTimer mMCdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (NetWorkUtil.isNetworkAvailable(this)) ;
        initMap(savedInstanceState);
        initView();
        mapPermission();
        monitorBlueTooth();
        String s = sHA1();
        Log.i("sha1", s);
        setCenter();
        gps_start(true);
        mMCdt = new CountDownTimer(100, 100) {
            @Override
            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {
                carStateHandler.post(mRunnable);
            }
        };
        mMCdt.start();
        if (mCenterPoint != null) {
            regeocdeQuery();
        }
    }

    /**
     * 监测蓝牙打开或者关闭
     */
    private void monitorBlueTooth() {
        mBlueToothStateReceiver = new BlueToothStateReceiver(this);
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        boolean isOpen = bluetoothAdapter.isEnabled();
        if (!isOpen){
            startActivity(new Intent(this, BlueToothActivity.class));
        }
        IntentFilter filter1;
        filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBlueToothStateReceiver,filter1);
        /*this.registerReceiver(mBlueToothStateReceiver,filter2);
        this.registerReceiver(mBlueToothStateReceiver,filter3);
        this.registerReceiver(mBlueToothStateReceiver,filter4);
        this.registerReceiver(mBlueToothStateReceiver,filter5);
        this.registerReceiver(mBlueToothStateReceiver,filter6);*/
    }

    /**
     * 查询当前是否在用车
     */
    public void isUserCar() {
        String userId = mSpUserinfo.getString("userId", "");
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).carStuts(userId);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson;
                    String jsonString = response.body().string();
                    Log.i("isUserCar", jsonString);
                    if (null != jsonString) {
                        gson = new Gson();
                        MessageBean messageBean = gson.fromJson(jsonString, MessageBean.class);
                        if (messageBean.getResponseCode().equals("1")) {
                            setCarStutsFragment();
                            mBtnUseCar.setText("我要还车");
                            gson = new Gson();
                            final CarStateBean carStateBean = gson.fromJson(jsonString, CarStateBean.class);
                            Log.i("carStateBean", carStateBean.toString());
                            if (null != carStateBean) {
                                sendCarValue(carStateBean);
                            }
                        } else {
                            carStateHandler.removeCallbacks(mRunnable);
                            setAdressInfoFragment();
                            mBtnUseCar.setText("我要用车");
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
     * 更新骑行状态
     *
     * @param carStateBean
     */
    private void sendCarValue(CarStateBean carStateBean) {
        Log.i("carStateBean1", carStateBean.toString());
        int cycleTime = carStateBean.getResponseObj().getCycleTime();
        int cycleCharge = carStateBean.getResponseObj().getCycleCharge();
        String carNo = carStateBean.getResponseObj().getCarNo();
        int carPower = carStateBean.getResponseObj().getCarPower();
        int useStatus = carStateBean.getResponseObj().getUseStatus();
        CarState carState = new CarState(cycleTime, cycleCharge, carNo, carPower, useStatus);
        EventBus.getDefault().post(new CarStateEvent(carState));
    }

    //设置屏幕中心点经纬度对应的地理位置的信息的fragment
    private void setAdressInfoFragment() {
        mFm = getSupportFragmentManager();
        mTransaction = mFm.beginTransaction();
        mAddressInfoFragment = new AddressInfoFragment();
        mTransaction.replace(R.id.fl_fragment_replace, mAddressInfoFragment);
        mTransaction.commitAllowingStateLoss();
    }

    private void initView() {
        mSpUserinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mGeocodeSearch = new GeocodeSearch(this);
        mGeocodeSearch.setOnGeocodeSearchListener(this);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mSp_isLogin = getSharedPreferences("isLogin", MODE_PRIVATE);
    }

    public String sHA1() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到屏幕的中心点经纬度
     */
    public void setCenter() {
            /*mCenterMarker = aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_point)));*/
        aMap.setOnCameraChangeListener(this);
        //  aMap.getMyLocation();

    }

    /**
     * 初始化地图
     *
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {
        mMap.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMap.getMap();
            setMap();
        }
        mInfoWinAdapter = new InfoWinAdapter(this);
        aMap.setInfoWindowAdapter(mInfoWinAdapter);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnMapClickListener(this);
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位时间间隔
            mLocationOption.setInterval(30000);
            // mLocationOption.setInterval(-1);
            //设置是否只定位一次,默认为false
           // mLocationOption.setOnceLocation(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    /**
     * 设置AMap的一些属性
     */
    private void setMap() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        aMap.setLocationSource(this);// 设置定位监听
        mUiSettings.setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        MyLocationStyle myLocationStyle = new MyLocationStyle();
            /*myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.end_point));// 设置小蓝点的图标*/
        myLocationStyle.strokeColor(Color.BLUE);// 设置圆形的边框颜色
        myLocationStyle.strokeWidth(3);
        //myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(getResources().getColor(R.color.tougrey1));// 设置圆形的填充颜色
        // myLocationStyle.radiusFillColor(color)//设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationRotateAngle(180);
    }

    @OnClick({R.id.iv_toolbarmore, R.id.iv_search, R.id.iv_gps_start, R.id.iv_bike_station, R.id.btn_use_car, R.id.iv_end_point})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbarmore:
                boolean isLogin = mSp_isLogin.getBoolean("isLogin", false);
                if (isLogin) {
                    startActivity(new Intent(this, UserInfoActivity.class));
                } else {
                    startActivity(new Intent(this, IsLoginActivity.class));
                }
                // startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.iv_search:
                startActivityForResult(new Intent(this, SearchActivity.class), REQUEST_CODE);
                break;
            case R.id.iv_gps_start:
                mapPermission();
                gps_start(false);
                break;
            case R.id.iv_bike_station:
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    if (mOldMarker != null) {
                        mOldMarker.hideInfoWindow();
                    }
                    if (mWalkRouteOverlay != null) {
                        mWalkRouteOverlay.removeFromMap();
                    }
                    if (!mIsStation) {
                        mIvBikeStation.setImageDrawable(getResources().getDrawable(R.drawable.station_icon));
                        queryCar(new LatLng(mAmapocation.getLatitude(), mAmapocation.getLongitude()));
                        mIsStation = true;
                    } else {
                        mIvBikeStation.setImageDrawable(getResources().getDrawable(R.drawable.moto));
                        queryCar(new LatLng(mAmapocation.getLatitude(), mAmapocation.getLongitude()));
                        mIsStation = false;
                    }
                }


                break;
            case R.id.btn_use_car:
                if (mBtnUseCar.getText().equals("我要用车")) {
                    userCar();
                } else if (mBtnUseCar.getText().equals("我要还车")) {
                    if (NetWorkUtil.isNetworkAvailable(this)) {
                        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_return_car, null);
                        final AlertDialog dialog = new AlertDialog.Builder(this).create();
                        dialog.setView(dialogView);
                        TextView tv_confim = (TextView) dialogView.findViewById(R.id.tv_confim);
                        tv_confim.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mProgersssDialog = new ProgersssDialog(MainActivity.this);
                                returnCar();
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    }
                }
                break;
            case R.id.iv_end_point:
                routeLine(mCenterPoint);
                break;
        }
    }

    private void gps_start(final boolean isFirst) {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            mapPermission();
            //isUserCar();
            isUserCar();
            if (mWalkRouteOverlay != null) {
                mWalkRouteOverlay.removeFromMap();
            }
            aMap.setLocationSource(this);// 设置定位监听
            aMap.setMyLocationEnabled(true);
            mlocationClient.startLocation();//启动定位

            mProgersssDialog = new ProgersssDialog(this);
            mProgersssDialog.setMsg("正在定位中");

            CountDownTimer cdt = new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (isOPen(MainActivity.this)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                    mProgersssDialog.dismiss();
                            }

                        }, 800);
                        if (mAmapocation == null) {
                            return;
                        }
                        double latitude = mAmapocation.getLatitude();
                        double longitude = mAmapocation.getLongitude();
                        if (latitude > 0) {
                            LatLng latlng = new LatLng(latitude, longitude);
                            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));
                            if (null != mCenterPoint) {
                                regeocdeQuery();
                            }
                        } else {
                            return;
                        }
                        Log.i("start", latitude + "" + longitude);
                    } else {
                        mProgersssDialog.dismiss();
                        ToastUtils.getShortToastByString(MainActivity.this, "定位失败,请打开gps");
                        openGPS(MainActivity.this);
                    }
                }

                @Override
                public void onFinish() {

                }
            };

            cdt.start();
        }
    }

    private void userCar() {
        boolean isLogin1 = mSp_isLogin.getBoolean("isLogin", false);
        boolean isDeposit = mSpUserinfo.getBoolean(getResources().getString(R.string.is_deposit), false);
        boolean isIdentify = mSpUserinfo.getBoolean(getResources().getString(R.string.is_identity), false);
        if (!isLogin1) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (!isDeposit) {
            startActivity(new Intent(this, DepositActivity.class));
            return;
        }
        if (!isIdentify) {
            startActivity(new Intent(this, IdentityVeritActivity.class));
            return;
        }
        if (isLogin1 && isDeposit && isIdentify) {
            QRCodePermission();
        }
    }

    public void returnCar() {
        String userId = mSpUserinfo.getString("userId", "");
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).returnCar(userId);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = response.body().string();
                    Log.i("returncar", jsonString);
                    if (null != jsonString) {
                        Gson gson = new Gson();
                        MessageBean messageBean = gson.fromJson(jsonString, MessageBean.class);
                        if (messageBean.getResponseCode().equals("1")) {
                            mProgersssDialog.dismiss();
                            startActivity(new Intent(MainActivity.this, EndRideActivity.class));
                            mBtnUseCar.setText("我要用车");
                            mlocationClient.startLocation();//启动定位
                            MainActivity.this.setAdressInfoFragment();
                        } else {
                            ToastUtil.show(MainActivity.this, messageBean.getResponseMsg());
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
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        carStateHandler.removeCallbacks(mRunnable);
        this.unregisterReceiver(mBlueToothStateReceiver);
    }

    @Override
    protected void onResume() {
        NetWorkUtil.isNetworkAvailable(this);
        super.onResume();
        mMap.onResume();
        monitorBlueTooth();
       // mMCdt.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean unlock = intent.getBooleanExtra("unlock", false);
        Log.i("unlock", unlock + "");
        if (unlock) {
            carStateHandler.post(mRunnable);
            setCarStutsFragment();
            mBtnUseCar.setText("我要还车");
        } else {
            mlocationClient.startLocation();
            setAdressInfoFragment();
            mBtnUseCar.setText("我要用车");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_CODE:
                LatLonPoint point = data.getParcelableExtra("point");
                if (point != null) {
                    LatLng latlng = new LatLng(point.getLatitude(), point.getLongitude());
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));
                    if (mCenterPoint!=null){
                        regeocdeQuery();
                    }
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMap.onPause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        regeocdeQuery();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
        isUserCar();
        regeocdeQuery();
    }

    /**
     * 激活定位
     *
     * @param listener
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        initLocation();
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        mAmapocation = amapLocation;
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                double longitude = amapLocation.getLongitude();
                double latitude = amapLocation.getLatitude();
                LatLng latlng = new LatLng(latitude, longitude);
                mLocalLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocalLatlng, 18));
                queryCar(latlng);
                //  aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 查找附近车的方法
     */
    public void queryCar(LatLng latlng) {
        Log.i("latlng1", latlng.toString());
        Point point = new Point(0, 0);
        LatLng start = aMap.getProjection().fromScreenLocation(point);
        int distance = testDistance(start, mCenterPoint);
        Log.i("distance", distance + "");
        Map<String, String> map = new HashMap<>();
        if (latlng != null) {
            map.put("radius", 1000 + "");
            map.put("longitude", latlng.longitude + "");
            map.put("latitude", latlng.latitude + "");
        }
        Call<ResponseBody> call = HttpUtil.getService(ApiService.class).queryCar(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        String stringJson = response.body().string();
                        if (stringJson != null) {
                            Gson gson = new Gson();
                            NearByCarBean nearByCarBean = gson.fromJson(stringJson, NearByCarBean.class);
                            if (nearByCarBean.getResponseCode().equals("1")) {
                                Log.i("nearby", nearByCarBean.toString());
                                List<NearByCarBean.ResponseObjBean> nearbyCars = nearByCarBean.getResponseObj();
                                mCar_amount = nearbyCars.size();
                                Log.i("car_amount", mCar_amount + "");
                                if (!mIsStation) {
                                    addBikeMark(nearbyCars);
                                } else {
                                    addStationMark(nearbyCars);
                                }
                            } else {
                                ToastUtil.show(MainActivity.this, nearByCarBean.getResponseMsg());
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * 给有车的位置加上标记
     *
     * @param cars
     */
    private void addBikeMark(List<NearByCarBean.ResponseObjBean> cars) {
        for (int i = 0; i < cars.size(); i++) {
            View markerVeiw = LayoutInflater.from(this).inflate(R.layout.layout_map_marker, null);
            double longitude = stringtoDouble(cars.get(i).getLongitude());
            double latitude = stringtoDouble(cars.get(i).getLatitude());
            LatLng latlng = new LatLng(latitude, longitude);
            aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .position(latlng)
                    .icon(BitmapDescriptorFactory.fromView(markerVeiw))
            );

        }

    }

    /**
     * 给有站点的位置加上标记
     *
     * @param cars
     */
    private void addStationMark(List<NearByCarBean.ResponseObjBean> cars) {
        for (int i = 0; i < cars.size(); i++) {
            View markerVeiw = LayoutInflater.from(this).inflate(R.layout.layout_map_marker, null);
            ImageView iv_station = (ImageView) markerVeiw.findViewById(R.id.iv_is_bike);
            iv_station.setImageDrawable(getResources().getDrawable(R.drawable.station_icon));
            double longitude = stringtoDouble(cars.get(i).getLongitude());
            double latitude = stringtoDouble(cars.get(i).getLatitude());
            LatLng latlng = new LatLng(latitude, longitude);
            aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .position(latlng)
                    .title("")
                    .setInfoWindowOffset(0, 8)
                    .icon(BitmapDescriptorFactory.fromView(markerVeiw))
            );

        }

    }

    private Double stringtoDouble(String string) {
        return Double.parseDouble(string);
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        LatLng target = cameraPosition.target;
        mCenterMarker.setPosition(target);

    }

    /**
     * 时时获取屏幕中心点的经纬度
     *
     * @param cameraPosition
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        //String s = cameraPosition.target.toStrig();
        mCenterPoint = cameraPosition.target;
        queryCar(new LatLng(mCenterPoint.latitude, mCenterPoint.longitude));
        regeocdeQuery();
    }

    private void regeocdeQuery() {
        LatLonPoint point = new LatLonPoint(mCenterPoint.latitude, mCenterPoint.longitude);
        Log.i("main1", mCenterPoint.toString());
        //ToastUtils.getShortToastByString(this,s);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
        mGeocodeSearch.getFromLocationAsyn(query);
    }

    //根据经纬度获取地理位置描述数据(逆地理编码结果回调）
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int errcode) {
        setInfoWindow(result, errcode);
        route();
    }

    /**
     * 计算规划路线
     */
    private void route() {
        aMap.setOnMarkerClickListener(this);
    }

    /**
     * marker的点击事件
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        mOldMarker = marker;
        routeLine(marker.getPosition());
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mOldMarker != null) {
            mOldMarker.hideInfoWindow();
        }
    }

    private void routeLine(LatLng endPoint) {
        double latitude = mAmapocation.getLatitude();
        double longitude = mAmapocation.getLongitude();
        final LatLonPoint startPoint = new LatLonPoint(latitude, longitude);
        if (mWalkRouteOverlay != null) {
            mWalkRouteOverlay.removeFromMap();
        }
        LatLng position = endPoint;
        LatLonPoint endpoint = new LatLonPoint(position.latitude, position.longitude);
        if (startPoint == null) {
            ToastUtils.getShortToastByString(MainActivity.this, "定位中,请稍后重试");
        } else if (endPoint == null) {
            ToastUtils.getShortToastByString(MainActivity.this, "终点未设置");
        } else {
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endpoint);
            //初始化query对象，fromAndTo是包含起终点信息，walkMode是步行路径规划的模式
            mQueryWalk = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WALK_DEFAULT);
            mRouteSearch.calculateWalkRouteAsyn(mQueryWalk);//开始算路
            showProgressDialog();
        }
    }

    /**
     * 设置地址信息窗口
     *
     * @param result
     * @param rCode
     */
    private void setInfoWindow(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                RegeocodeAddress regeocodeAddress = result.getRegeocodeAddress();
                regeocodeAddress.getFormatAddress();
                String province = regeocodeAddress.getProvince();
                String city = regeocodeAddress.getCity();
                String district = regeocodeAddress.getDistrict();
                String street = regeocodeAddress.getStreetNumber().getStreet() + "街";
                Log.i("street", street);
                double latitude = mAmapocation.getLatitude();
                double longitude = mAmapocation.getLongitude();
                LatLng start = new LatLng(latitude, longitude);
                int distance = testDistance(start, mCenterPoint);
                int time = distance / 100;
                Log.i("car_amount", mCar_amount + "");
                AddressInfo info = new AddressInfo(subString(regeocodeAddress.getFormatAddress(), province, city, district, street), mCar_amount + "", distance + "", time + "");
                Log.i("address", regeocodeAddress.getFormatAddress());
                EventBus.getDefault().post(new MyEvent(info));
            } else {
                ToastUtil.show(this, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    public String subString(String str, String province, String city, String distict, String street) {
        return str.substring(province.length() + distict.length() + city.length());
    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int i) {

    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    //规划步行路线
    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    mWalkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    mWalkRouteOverlay.removeFromMap();
                    mWalkRouteOverlay.addToMap();
                    mWalkRouteOverlay.zoomToSpan();
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(this, R.string.no_result);
                }
            } else {
                ToastUtil.show(this, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgersssDialog(this);
        progDialog.setMsg("路线规划中");
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    /**
     * 计算地图上两点的距离
     */
    public int testDistance(LatLng start, LatLng end) {
        int distance = (int) AMapUtils.calculateLineDistance(start, end);
        return distance;
    }

    /**
     * 设置车的状态的窗口
     */
    private void setCarStutsFragment() {
        mFm = getSupportFragmentManager();
        mTransaction = mFm.beginTransaction();
        mTransaction.replace(R.id.fl_fragment_replace, mCarStateFragment);
        mTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_MAP:
                break;
            case MY_PERMISSIONS_REQUEST_QR_CODE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_MAP:
                EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, "为了您能使用定位功能,请打开定位权限", R.string.confim, R.string.cancel, perms);
                break;
            case MY_PERMISSIONS_REQUEST_QR_CODE:
                EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, "为了您能使用扫码开锁功能,请打开照相机权限", R.string.confim, R.string.cancel, perms);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(MY_PERMISSIONS_REQUEST_MAP)
    public void mapPermission() {
        String[] mapPerms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
        if (EasyPermissions.hasPermissions(this.getApplicationContext(), mapPerms)) {
            initLocation();
        } else {
            EasyPermissions.requestPermissions(this, "请求定位权限!", MY_PERMISSIONS_REQUEST_MAP, mapPerms);
        }
    }

    @AfterPermissionGranted(MY_PERMISSIONS_REQUEST_QR_CODE)
    public void QRCodePermission() {
        String[] params = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this.getApplicationContext(), params)) {
            startActivity(new Intent(this, SweepLockActivity.class));
        } else {
            EasyPermissions.requestPermissions(this, "请求打开照相机权限!", MY_PERMISSIONS_REQUEST_QR_CODE, params);
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
