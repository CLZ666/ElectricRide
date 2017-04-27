package com.wanle.lequan.sharedbicycle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.MyNewsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/9.
 */

public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.ViewHolder> {

    private List<MyNewsBean> mData;
    private LayoutInflater mInflater;

    public MyNewsAdapter(Context context) {
        mData = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<MyNewsBean> data, boolean isRefresh) {
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
        View view = mInflater.inflate(R.layout.item_my_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyNewsBean bean = mData.get(position);
        holder.mTvTime.setText(bean.getTime());
        holder.mTvTitle.setText(bean.getTitle());
        holder.mTvContent.setText(bean.getContent());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_content)
        TextView mTvContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
