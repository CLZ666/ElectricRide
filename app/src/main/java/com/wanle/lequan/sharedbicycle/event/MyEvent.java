package com.wanle.lequan.sharedbicycle.event;

import com.wanle.lequan.sharedbicycle.bean.AddressInfo;
import com.wanle.lequan.sharedbicycle.bean.CarState;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/1.
 */

public class MyEvent {
    private String msg;
    private AddressInfo mInfo;
    private CarState mCarState;

    public MyEvent(String msg) {
        this.msg = msg;
    }

    public MyEvent(AddressInfo info) {
        this.mInfo = info;
    }


    public String getMsg() {
        return msg;
    }

    public AddressInfo getInfo() {
        return mInfo;
    }

    public void setInfo(AddressInfo info) {
        mInfo = info;
    }
}
