package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/16.
 */

public class CarState {
    private int cycleTime;
    private int cycleCharge;
    private String carNo;
    private int carPower;
    private int useStatus;

    public CarState(int cycleTime, int cycleCharge, String carNo, int carPower, int useStatus) {
        this.cycleTime = cycleTime;
        this.cycleCharge = cycleCharge;
        this.carNo = carNo;
        this.carPower = carPower;
        this.useStatus = useStatus;
    }

    public int getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(int cycleTime) {
        this.cycleTime = cycleTime;
    }

    public int getCycleCharge() {
        return cycleCharge;
    }

    public void setCycleCharge(int cycleCharge) {
        this.cycleCharge = cycleCharge;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public int getCarPower() {
        return carPower;
    }

    public void setCarPower(int carPower) {
        this.carPower = carPower;
    }

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    @Override
    public String toString() {
        return "CarState{" +
                "cycleTime=" + cycleTime +
                ", cycleCharge=" + cycleCharge +
                ", carNo='" + carNo + '\'' +
                ", carPower=" + carPower +
                ", useStatus=" + useStatus +
                '}';
    }
}

