package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/5/3.
 */

public class CdbBatteryBorrowReturnBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"operateType":1}
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
        /**
         * operateType : 1
         */

        private int operateType;

        public int getOperateType() {
            return operateType;
        }

        public void setOperateType(int operateType) {
            this.operateType = operateType;
        }
    }
}
