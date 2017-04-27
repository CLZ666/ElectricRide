package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/26.
 */

public class IntegralRecordBean {
    private String type;
    private String date;
    private String score;

    public IntegralRecordBean(String type, String date, String score) {
        this.type = type;
        this.date = date;
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getScore() {
        return score;
    }
}
