package com.wanle.lequan.sharedbicycle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.CouPonBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/9.
 */

public class CopPonRecordAdapter extends RecyclerView.Adapter<CopPonRecordAdapter.ViewHolder> {

    private List<CouPonBean.ResponseObjBean.RowsBean> mData;
    private LayoutInflater mInflater;

    public CopPonRecordAdapter(Context context) {
        mData = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<CouPonBean.ResponseObjBean.RowsBean> data, boolean isRefresh) {
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
        View view = mInflater.inflate(R.layout.item_coupon_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CouPonBean.ResponseObjBean.RowsBean bean = mData.get(position);
        holder.mTvTime.setText("有效期至:"+stampToDate(bean.getExpireTime()+""));
        holder.mTvMoney.setText("￥"+Integer.parseInt(bean.getDiscountMoney())/100+"元");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_youhui)
        TextView mTvYouhui;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_money)
        TextView mTvMoney;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
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
