package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:得到验证码的返回信息的bean
 * Date: 2017/2/24.
 */

public class VerificationCode {

    /**
     * responseCode : 1
     * responseMsg : 获取短信验证码成功
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
