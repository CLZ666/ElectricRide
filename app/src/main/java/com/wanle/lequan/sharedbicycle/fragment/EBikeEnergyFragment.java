package com.wanle.lequan.sharedbicycle.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.activity.NaviActivity;
import com.wanle.lequan.sharedbicycle.bean.NearByCarBean;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EBikeEnergyFragment extends Fragment implements GeocodeSearch.OnGeocodeSearchListener {


    @BindView(R.id.tv_battery_level)
    TextView mTvBatteryLevel;
    @BindView(R.id.tv_cdb_level)
    TextView mTvCdbLevel;
    NearByCarBean.ResponseObjBean mEbikeInfo;
    GeocodeSearch mGeocodeSearch;
    @BindView(R.id.tv_ebike_address)
    TextView mTvEbikeAddress;
    @BindView(R.id.btn_nav)
    Button mBtnNav;
    private LatLng mLocatePoint;

    public static EBikeEnergyFragment newInstance(NearByCarBean.ResponseObjBean ebikeInfo, LatLng locatePoint) {
        Bundle args = new Bundle();
        args.putParcelable("mEbikeInfo", ebikeInfo);
        args.putParcelable("mLocatePoint", locatePoint);
        EBikeEnergyFragment fragment = new EBikeEnergyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEbikeInfo = (NearByCarBean.ResponseObjBean) getArguments().get("mEbikeInfo");
        mLocatePoint = getArguments().getParcelable("mLocatePoint");
        mGeocodeSearch = new GeocodeSearch(getActivity());
        mGeocodeSearch.setOnGeocodeSearchListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ebike_energy, container, false);
        ButterKnife.bind(this, view);
        if (null != mEbikeInfo) {
            mBtnNav.setText("导航路线No." + mEbikeInfo.getCarNo());
            final int carPower = mEbikeInfo.getCarPower();
            if (carPower > 0 && carPower <= 10) {
                mTvBatteryLevel.setTextColor(getResources().getColor(R.color.colorRed1));
            } else if (carPower > 10 && carPower <= 50) {
                mTvBatteryLevel.setTextColor(getResources().getColor(R.color.colorYellow));
            } else {
                mTvBatteryLevel.setTextColor(getResources().getColor(R.color.colorGreen1));
        }
            mTvBatteryLevel.setText(mEbikeInfo.getCarPower() + "%");
        }
        regeocdeQuery();
        return view;
    }

    @OnClick(R.id.btn_nav)
    public void onClick() {
        if (null != mEbikeInfo) {
            LatLng endPoint = new LatLng(Double.parseDouble(mEbikeInfo.getLatitude()), Double.parseDouble(mEbikeInfo.getLongitude()));
            if (mLocatePoint != null) {
                NaviLatLng start = null, end = null;
                start = new NaviLatLng(mLocatePoint.latitude, mLocatePoint.longitude);
                end = new NaviLatLng(endPoint.latitude, endPoint.longitude);
                Intent intent = new Intent(getActivity(), NaviActivity.class);
                intent.putExtra("start", start);
                intent.putExtra("end", end);
                startActivity(intent);
            }
        }

    }

    private void regeocdeQuery() {
        if (null != mEbikeInfo) {
            LatLonPoint point = new LatLonPoint(Double.parseDouble(mEbikeInfo.getLatitude()), Double.parseDouble(mEbikeInfo.getLongitude()));
            //ToastUtils.getShortToastByString(this,s);
            // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
            mGeocodeSearch.getFromLocationAsyn(query);
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                RegeocodeAddress regeocodeAddress = result.getRegeocodeAddress();
                regeocodeAddress.getFormatAddress();
                String province = regeocodeAddress.getProvince();
                String city = regeocodeAddress.getCity();
                String district = regeocodeAddress.getDistrict();
                String street = regeocodeAddress.getStreetNumber().getStreet() + "街";
                String address = subString(regeocodeAddress.getFormatAddress(), province, city, district, street);
                mTvEbikeAddress.setText(address);
            } else {
                ToastUtil.show(getActivity(), R.string.no_result);
            }
        } else {
            ToastUtil.showerror(getActivity(), rCode);
        }
    }

    public String subString(String str, String province, String city, String distict, String street) {
        return str.substring(province.length() + distict.length() + city.length());
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
