package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/26.
 */

public class CouPonBean {
    private String youxiaotime;
    private String money;

    public CouPonBean(String youxiaotime, String money) {
        this.youxiaotime = youxiaotime;
        this.money = money;
    }

    public String getYouxiaotime() {
        return youxiaotime;
    }

    public String getMoney() {
        return money;
    }
}
