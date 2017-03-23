package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:登录接口返回的json数据转换为的bean
 * Date: 2017/2/24.
 */

public class loginBean {

    /**
     * responseCode : 1
     * responseObj : {"userId":"DA961CB39B60CDE164DC8B568220AC5BD40687D41E238F08AFE96DA695AE31086B2DC180FD294EC25F7DEBEB1F2B0DA7"}
     */

    private String responseCode;
    private ResponseObjBean responseObj;
    private String responseMsg;
    public String getResponseMsg() {
        return responseMsg;
    }
    @Override
    public String toString() {
        return "loginBean{" +
                "responseCode='" + responseCode + '\'' +
                ", responseObj=" + responseObj +
                ", responseMsg='" + responseMsg + '\'' +
                '}';
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public ResponseObjBean getResponseObj() {
        return responseObj;
    }

    public void setResponseObj(ResponseObjBean responseObj) {
        this.responseObj = responseObj;
    }

    public static class ResponseObjBean {
        /**
         * userId : DA961CB39B60CDE164DC8B568220AC5BD40687D41E238F08AFE96DA695AE31086B2DC180FD294EC25F7DEBEB1F2B0DA7
         */

        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "ResponseObjBean{" +
                    "userId='" + userId + '\'' +
                    '}';
        }
    }
}
