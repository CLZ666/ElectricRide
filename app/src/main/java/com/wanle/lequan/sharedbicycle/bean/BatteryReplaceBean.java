package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/26.
 */

public class BatteryReplaceBean {
    private int type;
    private String bno;
    private String time;
    private String replaceTime;

    public BatteryReplaceBean(int type, String bno, String time, String replaceTime) {
        this.type = type;
        this.bno = bno;
        this.time = time;
        this.replaceTime = replaceTime;
    }

    public int getType() {
        return type;
    }

    public String getBno() {
        return bno;
    }

    public String getTime() {
        return time;
    }

    public String getReplaceTime() {
        return replaceTime;
    }

}
