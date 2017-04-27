package com.wanle.lequan.sharedbicycle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.CdbLeaseRecordBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/21.
 */

public class LeaseRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CdbLeaseRecordBean> mData;
    private final LayoutInflater mInflater;

    public LeaseRecordAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
    }

    public static enum ITEM_TYPE {
        ITEM_IN_LEASE,
        ITEM_RETURNED;
    }

    public void setData(List<CdbLeaseRecordBean> data,boolean isRefresh) {
        if (null != data) {
            if (isRefresh){
                mData.clear();
            }
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_IN_LEASE.ordinal()) {
            return new inLeaseViewHolder(mInflater.inflate(R.layout.item_in_lease, parent, false));
        }else {
            return new outLeaseViewHolder(mInflater.inflate(R.layout.item_out_lease, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CdbLeaseRecordBean bean = mData.get(position);
        if (holder instanceof inLeaseViewHolder){
            ((inLeaseViewHolder) holder).mTvCarNo.setText("编号:"+bean.getCdbNo()+"");
            ((inLeaseViewHolder) holder).mTvLeaseTime.setText(bean.getLeaseTime());
            ((inLeaseViewHolder) holder).mTvLeaseCost.setText(bean.getLeaseSpend());
        } else if(holder instanceof outLeaseViewHolder){
            ((outLeaseViewHolder) holder).mTvCarNo.setText("编号:"+bean.getCdbNo()+"");
            ((outLeaseViewHolder) holder).mTvLeaseTime.setText(bean.getLeaseTime());
            ((outLeaseViewHolder) holder).mTvLeaseCost.setText(bean.getLeaseSpend());
        }
    }

   @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).getType() == 1) {
            return ITEM_TYPE.ITEM_IN_LEASE.ordinal();
        }
        return ITEM_TYPE.ITEM_RETURNED.ordinal();
    }

    static class inLeaseViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_car_no)
        TextView mTvCarNo;
        @BindView(R.id.tv_lease_time)
        TextView mTvLeaseTime;
        @BindView(R.id.tv_lease_cost)
        TextView mTvLeaseCost;
        @BindView(R.id.tv_lease_date)
        TextView mTvLeaseDate;

        inLeaseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class outLeaseViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_car_no)
        TextView mTvCarNo;
        @BindView(R.id.tv_lease_time)
        TextView mTvLeaseTime;
        @BindView(R.id.tv_lease_cost)
        TextView mTvLeaseCost;
        @BindView(R.id.tv_lease_date)
        TextView mTvLeaseDate;

        outLeaseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
