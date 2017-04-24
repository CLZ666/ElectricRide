package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/12.
 */

public class AddressInfo {
    private int type;
    private String srtreet;
    private String car_amount;
    private String distance;
    private String time;

    private AddressInfo(){
    }
    public AddressInfo(String street,String car_amount,String distance, String time) {
        this.srtreet = street;
        this.car_amount=car_amount;
        this.distance = distance;
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSrtreet() {
        return srtreet;
    }

    public void setSrtreet(String srtreet) {
        this.srtreet = srtreet;
    }

    public String getCar_amount() {
        return car_amount;
    }

    public void setCar_amount(String car_amount) {
        this.car_amount = car_amount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
