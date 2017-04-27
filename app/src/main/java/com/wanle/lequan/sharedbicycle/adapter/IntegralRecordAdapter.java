package com.wanle.lequan.sharedbicycle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.IntegralRecordBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/9.
 */

public class IntegralRecordAdapter extends RecyclerView.Adapter<IntegralRecordAdapter.ViewHolder> {
    private Context mContext;
    private List<IntegralRecordBean> mData;
    private LayoutInflater mInflater;

    public IntegralRecordAdapter(Context context) {
        this.mContext = context;
        mData = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<IntegralRecordBean> data, boolean isRefresh) {
        if (data != null) {
            if (isRefresh) {
                mData.clear();
            }
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_integral_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final IntegralRecordBean bean = mData.get(position);
        holder.mTvIntegralType.setText(bean.getType());
        holder.mTvIntegralTime.setText(bean.getDate());
        holder.mTvIntegralScore.setText(bean.getScore());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_integral_type)
        TextView mTvIntegralType;
        @BindView(R.id.tv_integral_time)
        TextView mTvIntegralTime;
        @BindView(R.id.tv_integral_score)
        TextView mTvIntegralScore;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
