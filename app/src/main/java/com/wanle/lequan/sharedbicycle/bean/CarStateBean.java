package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/16.
 */

public class CarStateBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"cycleTime":126,"cycleCharge":600,"carNo":"24929615696887808","carPower":100,"useStatus":2}
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
         * cycleTime : 126
         * cycleCharge : 600
         * carNo : 24929615696887808
         * carPower : 100
         * useStatus : 2
         */

        private int cycleTime;
        private int cycleCharge;
        private String carNo;
        private int carPower;
        private int useStatus;

        public int getCycleTime() {
            return cycleTime;
        }

        public void setCycleTime(int cycleTime) {
            this.cycleTime = cycleTime;
        }

        public int getCycleCharge() {
            return cycleCharge;
        }

        public void setCycleCharge(int cycleCharge) {
            this.cycleCharge = cycleCharge;
        }

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public int getCarPower() {
            return carPower;
        }

        public void setCarPower(int carPower) {
            this.carPower = carPower;
        }

        public int getUseStatus() {
            return useStatus;
        }

        public void setUseStatus(int useStatus) {
            this.useStatus = useStatus;
        }

        @Override
        public String toString() {
            return "ResponseObjBean{" +
                    "cycleTime=" + cycleTime +
                    ", cycleCharge=" + cycleCharge +
                    ", carNo='" + carNo + '\'' +
                    ", carPower=" + carPower +
                    ", useStatus=" + useStatus +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CarStateBean{" +
                "responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", responseObj=" + responseObj +
                '}';
    }
}
