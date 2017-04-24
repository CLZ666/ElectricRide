package com.wanle.lequan.sharedbicycle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.wanle.lequan.sharedbicycle.bean.CdbLeaseRecordBean;

import java.util.List;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/21.
 */

public class LeaseRecordAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private List<CdbLeaseRecordBean> mData;
    public LeaseRecordAdapter(Context context){
        this.mContext=context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }
}
