package com.wanle.lequan.sharedbicycle.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.ReturnCheckBean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/14.
 */

public class MyDialog {
    private Context mContext;

    public MyDialog(Context context) {
        this.mContext = context;
    }

    /**
     * 当在还车站点内显示的对话框
     */
    public void showInStation(ReturnCheckBean.ResponseObjBean.PlaceInBean placeBean, AMapLocation mlocation, FragmentManager fm,View.OnClickListener confimListener) {
        final AlertDialog dialogInStation = new AlertDialog.Builder(mContext).create();
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_in_station, null);
        TextView tv_address = (TextView) dialogView.findViewById(R.id.tv_station_address);
        ImageView iv_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
        Button btn_confim_return = (Button) dialogView.findViewById(R.id.btn_confim_return);
        dialogInStation.setView(dialogView);
        tv_address.setText(placeBean.getPlaceName());
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInStation.cancel();
            }
        });
        btn_confim_return.setOnClickListener(confimListener);
       // setReturnInStationFragment(placeBean,mlocation,fm);
        dialogInStation.show();
    }

   /* private void setReturnInStationFragment(ReturnCheckBean.ResponseObjBean.PlaceInBean placeBean, AMapLocation mlocation, FragmentManager fm) {
        ArrayList<ReturnCheckBean.ResponseObjBean.PlaceInBean.LongLatiJsonBean> points=new ArrayList<>();
        for (ReturnCheckBean.ResponseObjBean.PlaceInBean.LongLatiJsonBean latlng : placeBean.getLongLatiJson()) {
           points.add(latlng);
        }
        LatLng locatePoint=new LatLng(mlocation.getLatitude(),mlocation.getLongitude());
        final ReturnInStationFragment returnInStationFragment = ReturnInStationFragment.newInstance(locatePoint, points);
        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_fragment_replace1, returnInStationFragment);
        transaction.commitAllowingStateLoss();
    }*/

    /**
     * 当不在还车站点范围内显示的对话框
     */
    public void showOutStation(final View.OnClickListener confimlistener) {
        final AlertDialog dialogOutStation = new AlertDialog.Builder(mContext).create();
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_out_station, null);
        TextView tv_address = (TextView) dialogView.findViewById(R.id.tv_station_address);
        ImageView iv_station = (ImageView) dialogView.findViewById(R.id.iv_station);
        ImageView iv_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
        Button btn_confim_return = (Button) dialogView.findViewById(R.id.btn_confim_return);
        Button btn_follow_me = (Button) dialogView.findViewById(R.id.btn_follow_me);
        dialogOutStation.setView(dialogView);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOutStation.cancel();
            }
        });
        btn_confim_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialogReminder = new AlertDialog.Builder(mContext).create();
                View reminderView = LayoutInflater.from(mContext).inflate(R.layout.dialog_return_reminder, null);
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
                tv_confim_return.setOnClickListener(confimlistener);
                dialogReminder.show();
            }
        });
       /* btn_follow_me.setOnClickListener(new View.OnClickListener() {
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
        });*/
        dialogOutStation.show();
    }

}
