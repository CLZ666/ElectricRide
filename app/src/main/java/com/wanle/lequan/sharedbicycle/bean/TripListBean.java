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
     * responseObj : {"total":32,"page":4,"rows":[{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1493018195000,"id":"7d1a503eeec14f938247fd83b5ea2efd","itineraryJson":"","startTime":1493018172000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492667872000,"id":"5347ea4da8b14c77aa3ed682f747e397","itineraryJson":"","startTime":1492667862000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492666900000,"id":"a2f412eeca4e4df2be807d9675474d82","itineraryJson":"","startTime":1492666889000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492570007000,"id":"08e708e593ca4fbcba4f8196766a9380","itineraryJson":"","startTime":1492569992000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492566636000,"id":"e23d7e0f495e439f99a9e57149864b08","itineraryJson":"","startTime":1492566610000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492566593000,"id":"9ff3011c59e14408832ecd4759ef36ad","itineraryJson":"","startTime":1492566585000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492566569000,"id":"ba2a1878d67049359633f7ced059c5a8","itineraryJson":"","startTime":1492566559000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492566216000,"id":"a87e20383fee47abb2d3a8ca342094f1","itineraryJson":"","startTime":1492566169000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492565695000,"id":"3e9cb31493294a3094c69a3cc383619c","itineraryJson":"","startTime":1492565684000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":2,"endTime":1492565674000,"id":"7eb01c5d54f34ad69de1167f7c304f35","itineraryJson":"","startTime":1492565608000,"status":null,"userId":""}]}
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
         * total : 32
         * page : 4
         * rows : [{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1493018195000,"id":"7d1a503eeec14f938247fd83b5ea2efd","itineraryJson":"","startTime":1493018172000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492667872000,"id":"5347ea4da8b14c77aa3ed682f747e397","itineraryJson":"","startTime":1492667862000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492666900000,"id":"a2f412eeca4e4df2be807d9675474d82","itineraryJson":"","startTime":1492666889000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492570007000,"id":"08e708e593ca4fbcba4f8196766a9380","itineraryJson":"","startTime":1492569992000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492566636000,"id":"e23d7e0f495e439f99a9e57149864b08","itineraryJson":"","startTime":1492566610000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492566593000,"id":"9ff3011c59e14408832ecd4759ef36ad","itineraryJson":"","startTime":1492566585000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492566569000,"id":"ba2a1878d67049359633f7ced059c5a8","itineraryJson":"","startTime":1492566559000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492566216000,"id":"a87e20383fee47abb2d3a8ca342094f1","itineraryJson":"","startTime":1492566169000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":1,"endTime":1492565695000,"id":"3e9cb31493294a3094c69a3cc383619c","itineraryJson":"","startTime":1492565684000,"status":null,"userId":""},{"carId":"","carNo":"24929615696887809","cycleCharge":200,"cycleTime":2,"endTime":1492565674000,"id":"7eb01c5d54f34ad69de1167f7c304f35","itineraryJson":"","startTime":1492565608000,"status":null,"userId":""}]
         */

        private int total;
        private int page;
        private List<RowsBean> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * carId :
             * carNo : 24929615696887809
             * cycleCharge : 200
             * cycleTime : 1
             * endTime : 1493018195000
             * id : 7d1a503eeec14f938247fd83b5ea2efd
             * itineraryJson :
             * startTime : 1493018172000
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
}
