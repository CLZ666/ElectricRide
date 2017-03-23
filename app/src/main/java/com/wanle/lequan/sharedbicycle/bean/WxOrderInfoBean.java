package com.wanle.lequan.sharedbicycle.bean;

import com.google.gson.annotations.SerializedName;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/4.
 */

public class WxOrderInfoBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"prepayData":{"package":"Sign=WXPay","appid":"wx1902de5450f7a61e","sign":"31C733B628D88354BDE8DCCC773D9A1F","partnerid":"1369631402","prepayid":"wx20170305155746de11f402ee0643035579","noncestr":"20170305155514975536","timestamp":"1488700514"},"outTradeNo":"20170305155514975536","resultCode":1}
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
         * prepayData : {"package":"Sign=WXPay","appid":"wx1902de5450f7a61e","sign":"31C733B628D88354BDE8DCCC773D9A1F","partnerid":"1369631402","prepayid":"wx20170305155746de11f402ee0643035579","noncestr":"20170305155514975536","timestamp":"1488700514"}
         * outTradeNo : 20170305155514975536
         * resultCode : 1
         */

        private PrepayDataBean prepayData;
        private String outTradeNo;
        private int resultCode;

        public PrepayDataBean getPrepayData() {
            return prepayData;
        }

        public void setPrepayData(PrepayDataBean prepayData) {
            this.prepayData = prepayData;
        }

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public static class PrepayDataBean {
            /**
             * package : Sign=WXPay
             * appid : wx1902de5450f7a61e
             * sign : 31C733B628D88354BDE8DCCC773D9A1F
             * partnerid : 1369631402
             * prepayid : wx20170305155746de11f402ee0643035579
             * noncestr : 20170305155514975536
             * timestamp : 1488700514
             */

            @SerializedName("package")
            private String packageX;
            private String appid;
            private String sign;
            private String partnerid;
            private String prepayid;
            private String noncestr;
            private String timestamp;

            public String getPackageX() {
                return packageX;
            }

            public void setPackageX(String packageX) {
                this.packageX = packageX;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getPartnerid() {
                return partnerid;
            }

            public void setPartnerid(String partnerid) {
                this.partnerid = partnerid;
            }

            public String getPrepayid() {
                return prepayid;
            }

            public void setPrepayid(String prepayid) {
                this.prepayid = prepayid;
            }

            public String getNoncestr() {
                return noncestr;
            }

            public void setNoncestr(String noncestr) {
                this.noncestr = noncestr;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            @Override
            public String toString() {
                return "PrepayDataBean{" +
                        "packageX='" + packageX + '\'' +
                        ", appid='" + appid + '\'' +
                        ", sign='" + sign + '\'' +
                        ", partnerid='" + partnerid + '\'' +
                        ", prepayid='" + prepayid + '\'' +
                        ", noncestr='" + noncestr + '\'' +
                        ", timestamp='" + timestamp + '\'' +
                        '}';
            }
        }
    }
}
