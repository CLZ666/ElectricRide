package com.wanle.lequan.sharedbicycle.utils;

import android.content.Context;

import com.wanle.lequan.sharedbicycle.view.ProgersssDialog;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/21.
 */

public class PrgDiaUtil {
    static ProgersssDialog mProgersssDialog;
    public static void showPrg(Context context){
        mProgersssDialog=new ProgersssDialog(context);
    }

}
