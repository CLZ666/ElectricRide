package com.wanle.lequan.sharedbicycle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.BatteryReplaceBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/21.
 */

public class BatteryReplaceRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BatteryReplaceBean> mData;
    private final LayoutInflater mInflater;
    private BatteryReplaceRecordAdapter mAdapter;
    private List<BatteryReplaceBean> mdata;
    public BatteryReplaceRecordAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
    }

    public static enum ITEM_TYPE {
        ITEM_IN_REPLACE,
        ITEM_RETURNED;
    }

    public void setData(List<BatteryReplaceBean> data, boolean isRefresh) {
        if (null != data) {
            if (isRefresh) {
                mData.clear();
            }
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_IN_REPLACE.ordinal()) {
            return new inReplaceViewHolder(mInflater.inflate(R.layout.item_in_replace, parent, false));
        } else {
            return new outReplaceViewHolder(mInflater.inflate(R.layout.item_out_replace, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BatteryReplaceBean bean = mData.get(position);
        if (holder instanceof inReplaceViewHolder) {
            ((inReplaceViewHolder) holder).mTvBatteryNo.setText(bean.getBno());
            ((inReplaceViewHolder) holder).mTvLeaseTime.setText(bean.getTime());
            ((inReplaceViewHolder) holder).mTvLeaseDate.setText(bean.getReplaceTime());
        } else if (holder instanceof outReplaceViewHolder) {
            ((outReplaceViewHolder) holder).mTvBatteryNo.setText(bean.getBno());
            ((outReplaceViewHolder) holder).mTvLeaseTime.setText(bean.getTime());
            ((outReplaceViewHolder) holder).mTvLeaseDate.setText(bean.getReplaceTime());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).getType() == 1) {
            return ITEM_TYPE.ITEM_IN_REPLACE.ordinal();
        }
        return ITEM_TYPE.ITEM_RETURNED.ordinal();
    }

    static class inReplaceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_battery_no)
        TextView mTvBatteryNo;
        @BindView(R.id.tv_lease_time)
        TextView mTvLeaseTime;
        @BindView(R.id.tv_lease_date)
        TextView mTvLeaseDate;

        inReplaceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class outReplaceViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_battery_no)
        TextView mTvBatteryNo;
        @BindView(R.id.tv_lease_time)
        TextView mTvLeaseTime;
        @BindView(R.id.tv_lease_date)
        TextView mTvLeaseDate;

        outReplaceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
