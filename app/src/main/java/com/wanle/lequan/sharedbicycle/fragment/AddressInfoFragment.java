package com.wanle.lequan.sharedbicycle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.AddressInfo;
import com.wanle.lequan.sharedbicycle.event.MyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/12.
 */

public class AddressInfoFragment extends Fragment {
    @BindView(R.id.tv_street)
    TextView mTvStreet;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.tv_distance)
    TextView mTvDistance;
    @BindView(R.id.iv_bike)
    ImageView mIvBike;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Subscribe
    public void onEventMainThread(MyEvent event) {
        AddressInfo info = event.getInfo();
        if (null != info) {
            if (info.getType()==1){
                mTvAmount.setText("附近有充电基站" + info.getCar_amount() + "个");
                mIvBike.setImageDrawable(getResources().getDrawable(R.drawable.station));
            }else {
                mTvAmount.setText("剩余" + info.getCar_amount() + "辆");
                mIvBike.setImageDrawable(getResources().getDrawable(R.drawable.small_moto));
            }
            mTvStreet.setText(info.getSrtreet());
            mTvDistance.setText("距离" + info.getDistance() + "米");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
