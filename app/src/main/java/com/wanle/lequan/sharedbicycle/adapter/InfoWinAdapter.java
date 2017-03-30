package com.wanle.lequan.sharedbicycle.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.wanle.lequan.sharedbicycle.R;

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

    public InfoWinAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        initData(marker);
        View view = initView();
        return view;
    }

    private View initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.info_window_station, null);
        mIv_station = (ImageView) mView.findViewById(R.id.inf_win_station);
        mIv_station.setOnClickListener(this);
        return mView;
    }

    private void initData(Marker marker) {
        //mPosition = marker.getPosition();
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
        dialog.setView(dialogView);
        dialog.create().setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
