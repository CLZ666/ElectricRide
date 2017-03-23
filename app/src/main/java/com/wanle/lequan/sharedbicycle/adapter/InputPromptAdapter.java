package com.wanle.lequan.sharedbicycle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.wanle.lequan.sharedbicycle.R;

import java.util.ArrayList;
import java.util.List;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/10.
 */

public class InputPromptAdapter extends BaseAdapter{
    private Context mContext;
    private List<Tip> mData;
    public InputPromptAdapter(Context context){
        this.mContext=context;
        mData=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void setData(List<Tip> tips){
        if (null!=tips){
            mData.addAll(tips);
            notifyDataSetChanged();
        }
    }
    public void clearData(){
        mData.clear();
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_layout_input,parent,false);
            holder=new ViewHolder();
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else {
         holder= (ViewHolder) convertView.getTag();
        }
        Tip tip = mData.get(position);
        if (tip!=null){
            holder.tv_name.setText(tip.getName());
        }
        return convertView;
    }
    static class ViewHolder{
        private TextView tv_name;
    }
}
