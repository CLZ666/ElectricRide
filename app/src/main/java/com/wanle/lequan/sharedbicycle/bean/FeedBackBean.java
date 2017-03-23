package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/6.
 */

public class FeedBackBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {}
     */

    private String responseCode;
    private String responseMsg;
    private ResponseObjBean responseObj;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public ResponseObjBean getResponseObj() {
        return responseObj;
    }

    public void setResponseObj(ResponseObjBean responseObj) {
        this.responseObj = responseObj;
    }

    public static class ResponseObjBean {
    }
}
