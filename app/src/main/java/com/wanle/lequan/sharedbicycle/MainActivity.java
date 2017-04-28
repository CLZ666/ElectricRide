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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import com.amap.api.navi.model.NaviLatLng;
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
import com.wanle.lequan.sharedbicycle.activity.BaseActivity;
import com.wanle.lequan.sharedbicycle.activity.BlueToothActivity;
import com.wanle.lequan.sharedbicycle.activity.DepositActivity;
import com.wanle.lequan.sharedbicycle.activity.EndRideActivity;
import com.wanle.lequan.sharedbicycle.activity.IdentityVeritActivity;
import com.wanle.lequan.sharedbicycle.activity.IsLoginActivity;
import com.wanle.lequan.sharedbicycle.activity.LoginActivity;
import com.wanle.lequan.sharedbicycle.activity.NaviActivity;
import com.wanle.lequan.sharedbicycle.activity.SearchActivity;
import com.wanle.lequan.sharedbicycle.activity.SubmitProblemActivity;
import com.wanle.lequan.sharedbicycle.activity.SweepLockActivity;
import com.wanle.lequan.sharedbicycle.activity.UserInfoActivity;
import com.wanle.lequan.sharedbicycle.adapter.InfoWinAdapter;
import com.wanle.lequan.sharedbicycle.bean.AddressInfo;
import com.wanle.lequan.sharedbicycle.bean.CarState;
import com.wanle.lequan.sharedbicycle.bean.CarStateBean;
import com.wanle.lequan.sharedbicycle.bean.EndRideBean;
import com.wanle.lequan.sharedbicycle.bean.GlobalParmsBean;
import com.wanle.lequan.sharedbicycle.bean.MessageBean;
import com.wanle.lequan.sharedbicycle.bean.NearByCarBean;
import com.wanle.lequan.sharedbicycle.bean.NearByStationBean;
import com.wanle.lequan.sharedbicycle.bean.ReturnCheckBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.event.CarStateEvent;
import com.wanle.lequan.sharedbicycle.event.MyEvent;
import com.wanle.lequan.sharedbicycle.fragment.AddressInfoFragment;
import com.wanle.lequan.sharedbicycle.fragment.CarStateFragment;
import com.wanle.lequan.sharedbicycle.fragment.EBikeEnergyFragment;
import com.wanle.lequan.sharedbicycle.fragment.ReturnInStationFragment;
import com.wanle.lequan.sharedbicycle.overlay.WalkRouteOverlay;
import com.wanle.lequan.sharedbicycle.receiver.BlueToothStateReceiver;
import com.wanle.lequan.sharedbicycle.receiver.NetInfoReceiver;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;
import com.wanle.lequan.sharedbicycle.view.ProgersssDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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


public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, LocationSource, AMapLocationListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener, RouteSearch.OnRouteSearchListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener {
    @BindView(R.id.map)
    MapView mMap;
    @BindView(R.id.btn_use_car)
    TextView mBtnUseCar;
    @BindView(R.id.iv_end_point)
    ImageView mIvEndPoint;
    @BindView(R.id.iv_bike_station)
    ImageView mIvBikeStation;
    @BindView(R.id.iv_guide)
    ImageView mIvGuide;
    private SharedPreferences mSp_isLogin;
    private AMap aMap;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    OnLocationChangedListener mListener;
    //声明AMapLocationClient类对象
    public AMapLocationClient mlocationClient = null;
    AMapLocationClientOption mLocationOption;
    private LatLng mLocalLatlng;
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
    private boolean isShowEbikeEnergyFragment;
    private AddressInfoFragment mAddressInfoFragment;
    private EBikeEnergyFragment mEBikeEnergyFragment;
    private FragmentManager mFm;
    private FragmentTransaction mTransaction;
    private ProgersssDialog mProgersssDialog;
    private static final int MY_PERMISSIONS_REQUEST_MAP = 1;
    private static final int MY_PERMISSIONS_REQUEST_QR_CODE = 2;
    private int mCar_amount;
    private int chargeStationCount;
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
    private CountDownTimer mMCdt;
    private NetInfoReceiver mNetinfoReceiver;
    private String mStreet;
    private String mStreet1;
    private SharedPreferences mSpGlobalParms;
    private SharedPreferences mSpUserInfo;
    private List<Marker> bikeMarkers;
    private List<Marker> stationMarkers;
    private String mUserId;
    private AddressInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        NetWorkUtil.isNetworkAvailable(this);
        //monitiorMemoryLeak();
        initMap(savedInstanceState);
        initView();
        isUserCar();
        if (NetWorkUtil.isNetworkAvalible(this)) {
            mapPermission();
        }
        monitorBlueTooth();
        monitiorNetInfo();
        //String s = sHA1();
        //Log.i("sha1", s);
        setCenter();
        isUserCar();
        gps_start();
        mMCdt = new CountDownTimer(300, 300) {
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
        getGlobalParms();
    }

    /**
     * 获得全局参数
     */
    private void getGlobalParms() {
        mUserId = mSpUserInfo.getString("userId", "");
        Log.i("userId1", mUserId);
        final Call<ResponseBody> call = HttpUtil.getService(ApiService.class).globalParms(mUserId);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    final String jsonString = response.body().string();
                    if (null != jsonString) {
                        Gson gson = new Gson();
                        final GlobalParmsBean parmsBean = gson.fromJson(jsonString, GlobalParmsBean.class);
                        if (null != parmsBean) {
                            mSpGlobalParms.edit().putString("aboutUs", parmsBean.getResponseObj().getAboutUs()).commit();
                            mSpGlobalParms.edit().putString("customerService", parmsBean.getResponseObj().getCustomerService()).commit();
                            mSpGlobalParms.edit().putInt("deposit", parmsBean.getResponseObj().getDeposit()).commit();
                            mSpGlobalParms.edit().putString("userGuide", parmsBean.getResponseObj().getUserGuide()).commit();
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
     * 监测网络连接状态
     */
    private void monitiorNetInfo() {
        mNetinfoReceiver = new NetInfoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetinfoReceiver, filter);
    }

    /**
     * 获得网络信息接受者传来的消息,一旦有网络连接，立即定位到当前位置
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MyEvent event) {
        String msg = event.getMsg();
        // Log.i("eventmsg", msg);
        if (msg != null) {
            if (msg.equals("网络连接成功")) {
                if (null != mlocationClient && null != mCenterPoint) {
                    isUserCar();
                    gps_start();
                }
            }
        }
    }

    /**
     * 监测蓝牙打开或者关闭
     */
    private void monitorBlueTooth() {
        mBlueToothStateReceiver = new BlueToothStateReceiver(this);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null != bluetoothAdapter) {
            boolean isOpen = bluetoothAdapter.isEnabled();
            if (!isOpen) {
                startActivity(new Intent(this, BlueToothActivity.class));
            }
            IntentFilter filter;
            filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBlueToothStateReceiver, filter);
        }
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
                    String jsonString = null;
                    if (null != response) {
                        if (null != response.errorBody()) {
                            return;
                        }
                        if (response.code() == 200) {
                            if (null != response.body()) {
                                jsonString = response.body().string();
                                if (null != jsonString) {
                                    // Log.i("isUserCar", jsonString);
                                    gson = new Gson();
                                    MessageBean messageBean = gson.fromJson(jsonString, MessageBean.class);
                                    if (null != messageBean) {
                                        if (messageBean.getResponseCode().equals("1")) {
                                            // showChargeStation();
                                            mBtnUseCar.setText("我要还车");
                                            gson = new Gson();
                                            final CarStateBean carStateBean = gson.fromJson(jsonString, CarStateBean.class);
                                            if (null != carStateBean) {
                                                Log.i("carStateBean", carStateBean.toString());
                                                sendCarValue(carStateBean);
                                                setCarStutsFragment();
                                            }
                                        } else {
                                            carStateHandler.removeCallbacks(mRunnable);
                                            if (null != mlocationClient) {
                                                mlocationClient.startLocation();
                                            }
                                            regeocdeQuery();
                                            setAdressInfoFragment();
                                            showBike();
                                            mBtnUseCar.setText("我要用车");
                                        }
                                    }
                                }
                            }
                        } else {

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
        bikeMarkers = new ArrayList<>();
        stationMarkers = new ArrayList<>();
        mSpUserinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mGeocodeSearch = new GeocodeSearch(this);
        mGeocodeSearch.setOnGeocodeSearchListener(this);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mSp_isLogin = getSharedPreferences("isLogin", MODE_PRIVATE);
        EventBus.getDefault().register(this);
        mSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mSpGlobalParms = getSharedPreferences("global", MODE_PRIVATE);
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
     * 得到屏幕的中心点经纬度,设置中心点的marker
     */
    public void setCenter() {
        aMap.setOnCameraChangeListener(this);
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
                    .fromResource(R.drawable.location_icon));// 设置小蓝点的图标*/
        myLocationStyle.strokeColor(Color.BLUE);// 设置圆形的边框颜色
        myLocationStyle.strokeWidth(1);
        //myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(getResources().getColor(R.color.tougrey2));// 设置圆形的填充颜色

        // myLocationStyle.radiusFillColor(color)//设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        aMap.setMyLocationStyle(myLocationStyle);
        // aMap.setMyLocationRotateAngle(180);
    }

    @OnClick({R.id.iv_toolbarmore, R.id.iv_search, R.id.iv_gps_start, R.id.iv_submit_problem, R.id.iv_guide, R.id.iv_bike_station, R.id.btn_use_car, R.id.iv_end_point})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbarmore:
                boolean isLogin = mSp_isLogin.getBoolean("isLogin", false);
                if (isLogin) {
                    startActivity(new Intent(this, UserInfoActivity.class));
                } else {
                    startActivity(new Intent(this, IsLoginActivity.class));
                }
                break;
            case R.id.iv_search:
                startActivityForResult(new Intent(this, SearchActivity.class), REQUEST_CODE);
                //startActivityForResult(new Intent(this, DepositActivity.class), REQUEST_CODE);
                break;
            case R.id.iv_submit_problem:
                // callPhone();
                startActivity(new Intent(this, SubmitProblemActivity.class));
                break;
            case R.id.iv_gps_start:
                mapPermission();
                gps_start();
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
                        regeocdeQuery();
                        hideEBikeEnergyFragment();
                        showChargeStation();
                    } else {
                        regeocdeQuery();
                        showBike();
                    }
                }
                break;
            case R.id.btn_use_car:
                if (mBtnUseCar.getText().equals("我要用车")) {
                    userCar();
                } else if (mBtnUseCar.getText().equals("我要还车")) {
                    if (NetWorkUtil.isNetworkAvailable(this)) {
                        //toReturnBike();
                       /* mProgersssDialog = new ProgersssDialog(MainActivity.this);
                        mProgersssDialog.setMsg("正在还车中");
                        returnCar(1);*/
                        returnCheck();
                    }
                }
                break;
            case R.id.iv_end_point:
                if (null != mCenterPoint && null != mAmapocation) {
                    if (!equal(mAmapocation.getLatitude(), mCenterPoint.latitude)) {
                        mIvGuide.setVisibility(View.VISIBLE);
                    }
                }
                mIvGuide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aMap.setOnCameraChangeListener(null);
                        if (mAmapocation != null && mCenterPoint != null) {
                            NaviLatLng start = null, end = null;
                            start = new NaviLatLng(mAmapocation.getLatitude(), mAmapocation.getLongitude());
                            end = new NaviLatLng(mCenterPoint.latitude, mCenterPoint.longitude);
                            Intent intent = new Intent(MainActivity.this, NaviActivity.class);
                            intent.putExtra("start", start);
                            intent.putExtra("end", end);
                            startActivity(intent);
                            mIvGuide.setVisibility(View.GONE);
                            gps_start();
                        }
                    }
                });
                Log.i("mCenterPoint", mCenterPoint.toString());
                if (mCenterPoint != null && mCenterPoint.latitude > 0) {
                    routeLine(mCenterPoint);
                }
                break;
            default:
                break;
        }
    }

    public void callPhone() {
        final String phone = getSharedPreferences("global", MODE_PRIVATE).getString("customerService", "");
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));//跳转到拨号界面，同时传递电话号码
        startActivity(dialIntent);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    private void gps_start() {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            mapPermission();
            //isUserCar();
            mIvGuide.setVisibility(View.GONE);
            if (mWalkRouteOverlay != null) {
                mWalkRouteOverlay.removeFromMap();
            }
            if (mOldMarker != null) {
                mOldMarker.hideInfoWindow();
            }
            hideEBikeEnergyFragment();
            aMap.setLocationSource(this);// 设置定位监听
            aMap.setMyLocationEnabled(true);
            if (null != mlocationClient) {
                mlocationClient.startLocation();//启动定位
            }
            CountDownTimer cdt = new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (isOPen(MainActivity.this)) {
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

    /**
     * 显示附近充电站点
     */
    public void showChargeStation() {
        if (bikeMarkers.size() > 0) {
            for (Marker marker : bikeMarkers) {
                marker.remove();
            }
        }
        mIvBikeStation.setImageDrawable(getResources().getDrawable(R.drawable.station));
        if (null != mCenterPoint) {
            queryChargeStation(new LatLng(mCenterPoint.latitude, mCenterPoint.longitude));
        }
        mIsStation = true;
    }

    /**
     * 显示附近单车
     */
    public void showBike() {
        if (stationMarkers.size() > 0) {
            for (Marker marker : stationMarkers) {
                marker.remove();
            }
        }
        mIvBikeStation.setImageDrawable(getResources().getDrawable(R.drawable.moto));
        if (null != mCenterPoint) {
            queryCar(new LatLng(mCenterPoint.latitude, mCenterPoint.longitude));
        }
        mIsStation = false;
    }

    private void userCar() {
        boolean isLogin1 = mSp_isLogin.getBoolean("isLogin", false);
        boolean isDeposit = mSpUserinfo.getBoolean(getResources().getString(R.string.is_deposit), false);
        boolean isIdentify = mSpUserinfo.getBoolean(getResources().getString(R.string.is_identity), false);
        String blance = mSpUserinfo.getString("balance", "");
        double blance1 = 0;
        if (!"".equals(blance)) {
            blance1 = stringtoDouble(blance);
        }
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
        if (blance1 <= 0) {
            ToastUtils.getShortToastByString(this, "您的余额不足,请充值后使用哦");
            return;
        }
        if (isLogin1 && isDeposit && isIdentify && blance1 > 0) {
            QRCodePermission();
        }
    }

    /**
     * 还车校验
     */
    public void returnCheck() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", mUserId);
        map.put("longitude", "123");
        map.put("latitude", "33");
        final Call<ResponseBody> call = HttpUtil.getService(ApiService.class).returnCheck(map);
        GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        String jsonSting = response.body().string();
                        Gson gson = new Gson();
                        final ReturnCheckBean returnCheckBean = gson.fromJson(jsonSting, ReturnCheckBean.class);
                        if (null != returnCheckBean) {
                            if (returnCheckBean.getResponseCode().equals("1")) {
                                final String ifInStation = returnCheckBean.getResponseObj().getIfInStation();
                                if ("YES".equals(ifInStation)) {
                                    final ReturnCheckBean.ResponseObjBean.PlaceInBean placeInBean = returnCheckBean.getResponseObj().getPlaceIn().get(0);
                                    final FragmentManager fm = getSupportFragmentManager();
                                    if (null != mAmapocation) {
                                        LatLng locatePoint = new LatLng(mAmapocation.getLatitude(), mAmapocation.getLongitude());
                                        final ReturnInStationFragment inStationFragment = ReturnInStationFragment.newInstance(locatePoint, placeInBean);
                                        inStationFragment.show(fm.beginTransaction(), "inStation");
                                    }
                                } else {

                                }
                            } else {
                                ToastUtils.getShortToastByString(MainActivity.this, returnCheckBean.getResponseMsg());
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
     * 还车操作
     */
    public void toReturnBike() {
        if (stationMarkers.size() == 0) {
            ToastUtils.getShortToastByString(this, "附近没有还车站点");
            return;
        }
        int minDistance = 0;
        Marker nearEstMarker = null;
        for (int i = 0; i < stationMarkers.size(); i++) {
            Marker marker = stationMarkers.get(i);
            double latitude = marker.getPosition().latitude;
            double longitude = marker.getPosition().longitude;
            LatLng end = new LatLng(latitude, longitude);
            if (mAmapocation != null) {
                LatLng start = new LatLng(mAmapocation.getLatitude(), mAmapocation.getLongitude());
                int temp = testDistance(start, end);
                if (i == 0) {
                    minDistance = temp;
                    nearEstMarker = marker;
                }
                if (temp < minDistance) {
                    minDistance = temp;
                    nearEstMarker = marker;
                }
            }
        }
        Log.i("minDisatance", minDistance + "");
        if (null != nearEstMarker) {
            final NearByStationBean.ResponseObjBean markerInfo = (NearByStationBean.ResponseObjBean) nearEstMarker.getObject();
            if (minDistance > 0 && minDistance <= 100) {
                final AlertDialog dialogInStation = new AlertDialog.Builder(this).create();
                View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_in_station, null);
                TextView tv_address = (TextView) dialogView.findViewById(R.id.tv_station_address);
                ImageView iv_station = (ImageView) dialogView.findViewById(R.id.iv_station);
                ImageView iv_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
                Button btn_confim_return = (Button) dialogView.findViewById(R.id.btn_confim_return);
                dialogInStation.setView(dialogView);
                iv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogInStation.cancel();
                    }
                });
                if (null != markerInfo) {
                    tv_address.setText(markerInfo.getPlaceAddress());
                    //Glide.with(MainActivity.this).load(markerInfo.getPlaceImg()).into(iv_station);
                }
                btn_confim_return.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgersssDialog = new ProgersssDialog(MainActivity.this);
                        mProgersssDialog.setMsg("正在还车中");
                        returnCar(1);
                        dialogInStation.cancel();
                    }
                });
                dialogInStation.show();
            } else {
                final AlertDialog dialogOutStation = new AlertDialog.Builder(this).create();
                View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_out_station, null);
                TextView tv_address = (TextView) dialogView.findViewById(R.id.tv_station_address);
                ImageView iv_station = (ImageView) dialogView.findViewById(R.id.iv_station);
                ImageView iv_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
                Button btn_confim_return = (Button) dialogView.findViewById(R.id.btn_confim_return);
                Button btn_follow_me = (Button) dialogView.findViewById(R.id.btn_follow_me);
                dialogOutStation.setView(dialogView);
                if (null != markerInfo) {
                    tv_address.setText(markerInfo.getPlaceAddress());
                    //Glide.with(MainActivity.this).load(markerInfo.getPlaceImg()).into(iv_station);
                }
                iv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogOutStation.cancel();
                    }
                });
                btn_confim_return.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog dialogReminder = new AlertDialog.Builder(MainActivity.this).create();
                        View reminderView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_return_reminder, null);
                        dialogReminder.setView(reminderView);
                        TextView tv_return_cancel = (TextView) reminderView.findViewById(R.id.tv_return_cancel);
                        TextView tv_confim_return = (TextView) reminderView.findViewById(R.id.tv_confim_return);
                        tv_return_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogReminder.cancel();
                                dialogOutStation.cancel();
                            }
                        });
                        tv_confim_return.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mProgersssDialog = new ProgersssDialog(MainActivity.this);
                                mProgersssDialog.setMsg("正在还车中");
                                returnCar(2);
                                dialogReminder.cancel();
                                dialogOutStation.cancel();
                            }
                        });
                        dialogReminder.show();
                    }
                });
                btn_follow_me.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NaviLatLng start = null, end = null;
                        start = new NaviLatLng(mAmapocation.getLatitude(), mAmapocation.getLongitude());
                        end = new NaviLatLng(stringtoDouble(markerInfo.getLatitude()), stringtoDouble(markerInfo.getLongitude()));
                        Intent intent = new Intent(MainActivity.this, NaviActivity.class);
                        intent.putExtra("start", start);
                        intent.putExtra("end", end);
                        startActivity(intent);
                        dialogOutStation.cancel();
                    }
                });
                dialogOutStation.show();
            }
        }
    }

    public void returnCar(int isInStation) {
        String userId = mSpUserinfo.getString("userId", "");
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        if (null != mAmapocation) {
            final double longitude = mAmapocation.getLongitude();
            final double latitude = mAmapocation.getLatitude();
            map.put("longitude", longitude + "");
            map.put("latitude", latitude + "");
            map.put("isInStation", isInStation + "");
        }
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
                                mProgersssDialog.dismiss();
                                for (Marker marker : stationMarkers) {
                                    marker.remove();
                                }
                                Intent intent = new Intent(MainActivity.this, EndRideActivity.class);
                                intent.putExtra("endRide", endRideBean);
                                startActivity(intent);
                                mBtnUseCar.setText("我要用车");
                                showBike();
                                mlocationClient.startLocation();//启动定位
                                MainActivity.this.setAdressInfoFragment();
                            } else {
                                ToastUtil.show(MainActivity.this, endRideBean.getResponseMsg());
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
        stationMarkers = null;
        bikeMarkers = null;
        mMap.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        carStateHandler.removeCallbacks(mRunnable);
        this.unregisterReceiver(mBlueToothStateReceiver);
        this.unregisterReceiver(mNetinfoReceiver);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //NetWorkUtil.isNetworkAvailable(this);
        mMap.onResume();
        monitorBlueTooth();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean unlock = intent.getBooleanExtra("unlock", false);
        Log.i("unlock", unlock + "");
        if (unlock) {
            carStateHandler.post(mRunnable);
            setCarStutsFragment();
        } else {
            mMCdt = new CountDownTimer(100, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    carStateHandler.post(mRunnable);
                }

                @Override
                public void onFinish() {
                    carStateHandler.post(mRunnable);
                }
            };
            mMCdt.start();
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
                    if (mCenterPoint != null) {
                        regeocdeQuery();
                    }
                }
                break;
            default:
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
        isUserCar();
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
        if (NetWorkUtil.isNetworkAvailable(this)) {
            mListener = listener;
            mapPermission();
        }
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
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                mLocalLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                Log.i("locate", mLocalLatlng.toString());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocalLatlng, 18));
                if (!mIsStation) {
                    showBike();
                }
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
        if (null != latlng) {
            Log.i("latlng1", latlng.toString());
            Point point = new Point(0, 0);
            LatLng start = aMap.getProjection().fromScreenLocation(point);
            int distance = testDistance(start, mCenterPoint);
            Log.i("distance", distance + "");
            Map<String, String> map = new HashMap<>();
            map.put("radius", 1000 + "");
            map.put("longitude", latlng.longitude + "");
            map.put("latitude", latlng.latitude + "");
            Call<ResponseBody> call = HttpUtil.getService(ApiService.class).queryCar(map);
            GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        try {
                            String stringJson = response.body().string();
                            Log.i("near1", stringJson);
                            if (stringJson != null) {
                                Gson gson = new Gson();
                                NearByCarBean nearByCarBean = gson.fromJson(stringJson, NearByCarBean.class);
                                if (null != nearByCarBean) {
                                    if (nearByCarBean.getResponseCode().equals("1")) {
                                        Log.i("nearby", nearByCarBean.toString());
                                        List<NearByCarBean.ResponseObjBean> nearbyCars = nearByCarBean.getResponseObj();
                                        if (null != nearbyCars) {
                                            mCar_amount = nearbyCars.size();
                                            Log.i("car_amount", mCar_amount + "");
                                            addBikeMark(nearbyCars);
                                        }
                                    } else {
                                        ToastUtil.show(MainActivity.this, nearByCarBean.getResponseMsg());
                                    }
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

    }

    /**
     * 查找充电站点
     */
    public void queryChargeStation(LatLng latlng) {
        if (null != latlng) {
            Log.i("latlng1", latlng.toString());
            Point point = new Point(0, 0);
            LatLng start = aMap.getProjection().fromScreenLocation(point);
            int distance = testDistance(start, mCenterPoint);
            Log.i("distance", distance + "");
            Map<String, String> map = new HashMap<>();
            map.put("radius", 1000 + "");
            map.put("longitude", latlng.longitude + "");
            map.put("latitude", latlng.latitude + "");
            Log.i("returnStation", latlng.toString());
            Call<ResponseBody> call = HttpUtil.getService(ApiService.class).queryChargeStation(map);
            GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        try {
                            String stringJson = response.body().string();
                            Log.i("returnStation", stringJson);
                            if (stringJson != null) {
                                Gson gson = new Gson();
                                NearByStationBean nearByStationBean = gson.fromJson(stringJson, NearByStationBean.class);
                                if (null != nearByStationBean) {
                                    if (nearByStationBean.getResponseCode().equals("1")) {
                                        Log.i("nearby", nearByStationBean.toString());
                                        List<NearByStationBean.ResponseObjBean> nearbyStations = nearByStationBean.getResponseObj();
                                        if (null != nearbyStations) {
                                            addChargeMark(nearbyStations);
                                        }
                                    } else {
                                        ToastUtil.show(MainActivity.this, nearByStationBean.getResponseMsg());
                                    }
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
    }


    /**
     * 给有车的位置加上标记
     *
     * @param cars
     */
    private void addBikeMark(List<NearByCarBean.ResponseObjBean> cars) {
        for (Marker marker : stationMarkers) {
            marker.remove();
        }
        //bikeMarkers.clear();
        if (cars != null) {
            for (int i = 0; i < cars.size(); i++) {
                final NearByCarBean.ResponseObjBean nearCarBean = cars.get(i);
                View markerVeiw = LayoutInflater.from(this).inflate(R.layout.layout_map_marker, null);
                ImageView ivBike = (ImageView) markerVeiw.findViewById(R.id.iv_bike);
                double longitude = stringtoDouble(nearCarBean.getLongitude());
                double latitude = stringtoDouble(nearCarBean.getLatitude());
                LatLng latlng = new LatLng(latitude, longitude);
                final int carPower = nearCarBean.getCarPower();
                Marker marker = null;
                if (carPower > 0 && carPower <= 10) {
                    ivBike.setImageResource(R.drawable.bike_icon_red);
                    marker = aMap.addMarker(new MarkerOptions()
                            .anchor(0.5f, 0.5f)
                            .position(latlng)
                            .icon(BitmapDescriptorFactory.fromView(markerVeiw))
                    );
                } else if (carPower > 10 && carPower <= 50) {
                    ivBike.setImageResource(R.drawable.bike_icon_yellow);
                    marker = aMap.addMarker(new MarkerOptions()
                            .anchor(0.5f, 0.5f)
                            .position(latlng)
                            .icon(BitmapDescriptorFactory.fromView(markerVeiw))
                    );
                } else {
                    ivBike.setImageResource(R.drawable.bike_icon_green);
                    marker = aMap.addMarker(new MarkerOptions()
                            .anchor(0.5f, 0.5f)
                            .position(latlng)
                            .icon(BitmapDescriptorFactory.fromView(markerVeiw))
                    );
                }
                marker.setObject(cars.get(i));
                bikeMarkers.add(marker);
            }
        }

    }

    /**
     * 给有充电站点的位置加上标记
     */
    private void addChargeMark(List<NearByStationBean.ResponseObjBean> stations) {
        Log.i("stations", stations.toString());
        for (Marker marker : bikeMarkers) {
            marker.remove();
        }
        chargeStationCount = stations.size();
        for (int i = 0; i < stations.size(); i++) {
            View markerVeiw = LayoutInflater.from(this).inflate(R.layout.layout_map_marker, null);
            ImageView iv_station = (ImageView) markerVeiw.findViewById(R.id.iv_bike);
            iv_station.setImageDrawable(getResources().getDrawable(R.drawable.station_icon));
            double longitude = stringtoDouble(stations.get(i).getLongitude());
            double latitude = stringtoDouble(stations.get(i).getLatitude());
            LatLng latlng = new LatLng(latitude, longitude);
            Marker marker = aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .position(latlng)
                    .title("charge")
                    .setInfoWindowOffset(0, 8)
                    .icon(BitmapDescriptorFactory.fromView(markerVeiw))
            );
            marker.setObject(stations.get(i));
            stationMarkers.add(marker);
        }

    }

    private Double stringtoDouble(String string) {
        return Double.parseDouble(string);
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
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
        if (!mIsStation) {
            queryCar(new LatLng(mCenterPoint.latitude, mCenterPoint.longitude));
        } else {
            queryChargeStation(new LatLng(mCenterPoint.latitude, mCenterPoint.longitude));
        }
        regeocdeQuery();
    }

    private void regeocdeQuery() {
        if (null != mCenterPoint) {
            LatLonPoint point = new LatLonPoint(mCenterPoint.latitude, mCenterPoint.longitude);
            Log.i("main1", mCenterPoint.toString());
            //ToastUtils.getShortToastByString(this,s);
            // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
            mGeocodeSearch.getFromLocationAsyn(query);
        }
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
     * marker的点击事件,和导航
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            mOldMarker = marker;
            routeLine(marker.getPosition());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCenterPoint = marker.getPosition();
                    regeocdeQuery();
                }
            }, 800);

            final String title = marker.getOptions().getTitle();
            if (null == title) {
                showEBikeEnergyFragment(marker);
            }
            mIvGuide.setVisibility(View.VISIBLE);
            mIvGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAmapocation != null && mCenterPoint != null) {
                        NaviLatLng start = null, end = null;
                        start = new NaviLatLng(mAmapocation.getLatitude(), mAmapocation.getLongitude());
                        end = new NaviLatLng(mCenterPoint.latitude, mCenterPoint.longitude);
                        Intent intent = new Intent(MainActivity.this, NaviActivity.class);
                        intent.putExtra("start", start);
                        intent.putExtra("end", end);
                        startActivity(intent);
                        gps_start();
                        mIvGuide.setVisibility(View.GONE);
                    }
                }
            });
        }
        return false;
    }

    /**
     * 显示每辆车的电池和充电宝信息
     */
    private void showEBikeEnergyFragment(Marker marker) {
        isShowEbikeEnergyFragment = true;
        NearByCarBean.ResponseObjBean bean = (NearByCarBean.ResponseObjBean) marker.getObject();
        if (null != mAmapocation && null != bean) {
            LatLng locatePoint = new LatLng(mAmapocation.getLatitude(), mAmapocation.getLongitude());
            mFm = getSupportFragmentManager();
            mTransaction = mFm.beginTransaction();
            mEBikeEnergyFragment = EBikeEnergyFragment.newInstance(bean, locatePoint);
            mTransaction.replace(R.id.fl_ebike_energy_replace, mEBikeEnergyFragment);
            mTransaction.commitAllowingStateLoss();
        }
    }

    /**
     * 隐藏每辆车的电池和充电宝信息
     */
    private void hideEBikeEnergyFragment() {
        isShowEbikeEnergyFragment = false;
        mFm = getSupportFragmentManager();
        mTransaction = mFm.beginTransaction();
        if (null != mEBikeEnergyFragment) {
            mTransaction.remove(mEBikeEnergyFragment);
            mTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            if (isShowEbikeEnergyFragment) {
                isShowEbikeEnergyFragment = false;
                hideEBikeEnergyFragment();
            } else {
                if (mOldMarker != null) {
                    mOldMarker.hideInfoWindow();
                }
                if (null != mWalkRouteOverlay) {
                    mWalkRouteOverlay.removeFromMap();
                }
                mIvGuide.setVisibility(View.GONE);
            }
        }
    }

    private void routeLine(LatLng endPoint) {
        if (null != endPoint && null != mAmapocation) {
            Log.i("mCenterPoint", mAmapocation.getLatitude() + "");
            double latitude = mAmapocation.getLatitude();
            double longitude = mAmapocation.getLongitude();
            final LatLonPoint startPoint = new LatLonPoint(latitude, longitude);
            if (mWalkRouteOverlay != null) {
                mWalkRouteOverlay.removeFromMap();
            }
            LatLng position = endPoint;
            final double latitude1 = position.latitude;
            if (!equal(latitude1, latitude)) {
                LatLonPoint endpoint = new LatLonPoint(position.latitude, position.longitude);
                RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endpoint);
                //初始化query对象，fromAndTo是包含起终点信息，walkMode是步行路径规划的模式
                mQueryWalk = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WALK_DEFAULT);
                mRouteSearch.calculateWalkRouteAsyn(mQueryWalk);//开始算路
                showProgressDialog();
            }

        }

    }

    public boolean equal(double num1, double num2) {
        if ((num1 - num2 > -0.00001) && (num1 - num2) < 0.00001) {
            return true;
        } else return false;
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
                mStreet = regeocodeAddress.getStreetNumber().getStreet() + "街";
                Log.i("street", mStreet);
                double latitude = mAmapocation.getLatitude();
                double longitude = mAmapocation.getLongitude();
                LatLng start = new LatLng(latitude, longitude);
                int distance = testDistance(start, mCenterPoint);
                int time = distance / 100;
                Log.i("car_amount", mCar_amount + "");
                mStreet1 = subString(regeocodeAddress.getFormatAddress(), province, city, district, mStreet);
                if (mIsStation) {
                    mInfo = new AddressInfo(1, mStreet1, chargeStationCount + "", distance + "", time + "");
                } else {
                    mInfo = new AddressInfo(2, mStreet1, mCar_amount + "", distance + "", time + "");
                }
                Log.i("address", regeocodeAddress.getFormatAddress());
                EventBus.getDefault().post(new MyEvent(mInfo));
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
                } else if (result.getPaths() == null) {
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
