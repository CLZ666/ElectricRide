package com.wanle.lequan.sharedbicycle.bean;

import java.util.List;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/1.
 */

public class TripListBean {


    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : [{"carId":"","carNo":"24929615696887808","cycleCharge":2,"cycleTime":7,"endTime":1487647852000,"id":"bc80e9fcf7e411e6b1218e34576bcc2e","itineraryJson":"","startTime":1487647430000,"status":null,"userId":""}]
     */

    private String responseCode;
    private String responseMsg;
    private List<ResponseObjBean> responseObj;

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

    public List<ResponseObjBean> getResponseObj() {
        return responseObj;
    }

    public void setResponseObj(List<ResponseObjBean> responseObj) {
        this.responseObj = responseObj;
    }

    public static class ResponseObjBean {
        /**
         * carId :
         * carNo : 24929615696887808
         * cycleCharge : 2
         * cycleTime : 7
         * endTime : 1487647852000
         * id : bc80e9fcf7e411e6b1218e34576bcc2e
         * itineraryJson :
         * startTime : 1487647430000
         * status : null
         * userId :
         */

        private String carId;
        private String carNo;
        private int cycleCharge;
        private int cycleTime;
        private long endTime;
        private String id;
        private String itineraryJson;
        private long startTime;
        private Object status;
        private String userId;

        public String getCarId() {
            return carId;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public int getCycleCharge() {
            return cycleCharge;
        }

        public void setCycleCharge(int cycleCharge) {
            this.cycleCharge = cycleCharge;
        }

        public int getCycleTime() {
            return cycleTime;
        }

        public void setCycleTime(int cycleTime) {
            this.cycleTime = cycleTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getItineraryJson() {
            return itineraryJson;
        }

        public void setItineraryJson(String itineraryJson) {
            this.itineraryJson = itineraryJson;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
