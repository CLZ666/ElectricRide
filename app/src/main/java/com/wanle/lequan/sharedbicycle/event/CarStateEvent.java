package com.wanle.lequan.sharedbicycle.event;

import com.wanle.lequan.sharedbicycle.bean.CarState;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/17.
 */

public class CarStateEvent {
    private CarState mCarState;
    public CarStateEvent(CarState carState){
        this.mCarState=carState;
    }

    public CarState getCarState() {
        return mCarState;
    }

    public void setCarState(CarState carState) {
        mCarState = carState;
    }

    @Override
    public String toString() {
        return "CarStateEvent{" +
                "mCarState=" + mCarState +
                '}';
    }
}
