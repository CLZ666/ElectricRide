package com.wanle.lequan.sharedbicycle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.activity.TripDetailActivity;
import com.wanle.lequan.sharedbicycle.bean.TripListBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * autor:Jerry
 * fuction:行程列表适配器
 * Date: 2017/3/1.
 */

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.MyViewHolder> {

    private Context mContext;
    private List<TripListBean.ResponseObjBean> mDatas;

    public TripListAdapter(Context context) {
        this.mContext = context;
        mDatas = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_layout_trip, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    public void setData(List<TripListBean.ResponseObjBean> datas,boolean isRefresh) {
        if (datas != null) {
            if (isRefresh){
                mDatas.clear();
            }
            Log.i("trip1",datas.size()+"");
            mDatas.addAll(datas);
             notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final TripListBean.ResponseObjBean dataBean = mDatas.get(position);
        double CycleCharge = dataBean.getCycleCharge() * 1.0 / 100;
        if (dataBean != null) {
            holder.mTvDate.setText(stampToDate(dataBean.getStartTime() + ""));
            holder.mTvCarNo.setText(dataBean.getCarNo());
            holder.mTvTripTime.setText(dataBean.getCycleTime() + " "+"分钟");
            holder.mTvTripSpend.setText(CycleCharge + " "+"元");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itineraryId=dataBean.getId();
                Intent intent=new Intent(mContext, TripDetailActivity.class);
                intent.putExtra("itineraryId",itineraryId);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size() == 0 ? 0 : mDatas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvDate;

        private TextView mTvCarNo;

        private TextView mTvTripTime;

        private TextView mTvTripSpend;

        public MyViewHolder(View itemView) {
            super(itemView);
            //ButterKnife.bind(itemView);
            mTvDate = (TextView) itemView.findViewById(R.id.tv_date);
            mTvCarNo = (TextView) itemView.findViewById(R.id.tv_car_no);
            mTvTripTime = (TextView) itemView.findViewById(R.id.tv_trip_time);
            mTvTripSpend = (TextView) itemView.findViewById(R.id.tv_trip_spend);
        }
    }

    /*
    * 将时间戳转换为时间
    */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
