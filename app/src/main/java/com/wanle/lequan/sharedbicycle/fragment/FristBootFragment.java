package com.wanle.lequan.sharedbicycle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wanle.lequan.sharedbicycle.R;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/5.
 */

public class FristBootFragment extends Fragment {
    private  int mType;


    public static FristBootFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type",type);
        FristBootFragment fragment = new FristBootFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         Bundle arguments = getArguments();
        mType= arguments.getInt("type");
        Log.i("type1",mType+"");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_boot, container, false);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageView mIv_first_boot = (ImageView) view.findViewById(R.id.iv_first_boot);
        setImg(mIv_first_boot);
        return view;
    }

    private void setImg(ImageView mIv_first_boot) {
        switch (mType){
            case 0:
                mIv_first_boot.setImageResource(R.drawable.boot_interface1);
                break;
            case 1:
                mIv_first_boot.setImageResource(R.drawable.boot_interface2);
                break;
            case 2:
                mIv_first_boot.setImageResource(R.drawable.boot_interface3);
                break;
            case 3:
                mIv_first_boot.setImageResource(R.drawable.boot_interface4);
                break;
            default:
                break;
        }
    }
}
