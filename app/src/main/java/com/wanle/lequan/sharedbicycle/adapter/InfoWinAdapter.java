package com.wanle.lequan.sharedbicycle.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.bumptech.glide.Glide;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.NearByStationBean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/21.
 */

public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {

    //private LatLng mPosition;
    private Context mContext;
    private View mView;
    private ImageView mIv_station;
    private NearByStationBean.ResponseObjBean station;
    public InfoWinAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = initView();
        setViewContent(view,marker);
        return view;
    }

    private void setViewContent(View view, Marker marker) {
        station= (NearByStationBean.ResponseObjBean) marker.getObject();
        mIv_station = (ImageView) view.findViewById(R.id.inf_win_station);
        TextView tv_station_name= (TextView) view.findViewById(R.id.tv_station_name);
        TextView tv_station_address= (TextView) view.findViewById(R.id.tv_station_address);
        if (null!=station){
            tv_station_name.setText(station.getPlaceName());
            tv_station_address.setText(station.getPlaceAddress());
            if (!station.getPlaceImg().equals("")){
                Glide.with(mContext).load(station.getPlaceImg()).into(mIv_station);
            }
            mIv_station.setOnClickListener(this);
        }
    }


    private View initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.info_window_station, null);
        return mView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.MyDialogStyle);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_inf_win, null);
       ImageView iv= (ImageView) dialogView.findViewById(R.id.inf_win_station);
        iv.setImageDrawable(mIv_station.getDrawable());
        if (!station.getPlaceImg().equals("")){
            Glide.with(mContext).load(station.getPlaceImg()).into(mIv_station);
        }
        dialog.setView(dialogView);
        dialog.create().setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
