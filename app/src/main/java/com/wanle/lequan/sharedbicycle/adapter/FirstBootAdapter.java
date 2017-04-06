package com.wanle.lequan.sharedbicycle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.wanle.lequan.sharedbicycle.fragment.FitstBootEndFragment;
import com.wanle.lequan.sharedbicycle.fragment.FristBootFragment;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/5.
 */

public class FirstBootAdapter extends FragmentPagerAdapter {


    public FirstBootAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("position",position+"");
        if (position==4){
            return FitstBootEndFragment.newInstance();
        }else{
            return FristBootFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
