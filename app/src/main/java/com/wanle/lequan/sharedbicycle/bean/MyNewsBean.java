package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/26.
 */

public class MyNewsBean {
    private String time;
    private String title;
    private String content;

    public MyNewsBean(String time, String title, String content) {
        this.time = time;
        this.title = title;
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
