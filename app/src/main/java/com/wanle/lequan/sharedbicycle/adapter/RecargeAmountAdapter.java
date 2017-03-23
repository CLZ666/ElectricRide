package com.wanle.lequan.sharedbicycle.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.event.MyEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/5.
 */

public class RecargeAmountAdapter extends RecyclerView.Adapter<RecargeAmountAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> mData;
    private int lastPressIndex = -1;
    private MyViewHolder mHolder;
    private int mPosition;
    public RecargeAmountAdapter(Context context) {
        this.mContext = context;
        mData=new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_layout_recharge, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mHolder=holder;
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<String> data) {
        Log.i("recharge",data.toString());
        if (data!=null){
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public MyViewHolder(final View itemView) {
            super(itemView);
            tv=(TextView)itemView.findViewById(R.id.tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TAG", "OneViewHolder: ");
                    int position = getAdapterPosition();
                    mPosition=position;
                    if (lastPressIndex == position) {
                        lastPressIndex = -1;
                    } else {
                        lastPressIndex = position;
                    }
                    notifyDataSetChanged();
                    CharSequence text = tv.getText();
                    EventBus.getDefault().post(new MyEvent(text.toString()));
                }
            });
        }
        public  void setData(Object data){
            if (data != null) {
                String text = (String) data;
                tv.setText(text);
                if (getAdapterPosition() == lastPressIndex) {
                    tv.setSelected(true);
                    tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));

                } else {
                    tv.setSelected(false);
                    tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.blue_500));
                }

            }
        }

    }

    public void changeTextColor(String msg) {
        if (msg.equals("editNotEmpty")){
            lastPressIndex=-1;
            mHolder.setData(mData.get(mPosition));
            notifyDataSetChanged();
        }
    }

}
