package com.wanle.lequan.sharedbicycle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.RechargeRecordBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/9.
 */

public class RechargeRecordAdapter extends BaseAdapter {
    private Context mContext;
    private List<RechargeRecordBean.ResponseObjBean.ListBean> mData;

    public RechargeRecordAdapter(Context context) {
        this.mContext = context;
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public void setData(List<RechargeRecordBean.ResponseObjBean.ListBean> data, boolean isRefresh) {
        if (data != null) {
            if (isRefresh) {
                mData.clear();
            }
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recharge_record, parent, false);
            holder = new ViewHolder();
            holder.mTvType = (TextView) convertView.findViewById(R.id.tv_type);
            holder.mTvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.mTvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RechargeRecordBean.ResponseObjBean.ListBean dataBean = mData.get(position);
        int income = dataBean.getIncome();
        String type = dataBean.getType();
        final int pay = dataBean.getPay();
        if (type .equals("4")) {
            double money = (pay * 1.0) / 100;
            holder.mTvAmount.setText("-" + money + "");
        } else {
            double money = (income * 1.0) / 100;
            holder.mTvAmount.setText("+" + money + "");
        }
        holder.mTvTime.setText(stampToDate(dataBean.getCtime()));
        switch (type) {
            case "1":
                holder.mTvType.setText("充值增加");
                break;
            case "2":
                holder.mTvType.setText("充值押金");
                break;
            case "3":
                holder.mTvType.setText("退还押金");
                break;
            case "4":
                holder.mTvType.setText("消费支出");
                break;
            case "7":
                holder.mTvType.setText("活动奖励");
                break;
            default:
                break;
        }
        return convertView;
    }


    static class ViewHolder {

        TextView mTvType;

        TextView mTvTime;

        TextView mTvAmount;
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
}
