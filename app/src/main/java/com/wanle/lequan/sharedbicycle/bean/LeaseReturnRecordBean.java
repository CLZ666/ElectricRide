package com.wanle.lequan.sharedbicycle.bean;

import java.util.List;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/5/3.
 */

public class LeaseReturnRecordBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"total":2,"page":1,"rows":[{"phone":"18205253808","stationNo":"181139437395","createTime":1493174835000,"stationType":2,"userType ":0,"userName":"","type":1,"hourTotal":5,"cycleCharge":200},{"phone":"18205253808","stationNo":"181139437395","createTime":1493173707000,"stationType":2,"userType":0,"userName":"","type":2,"hourTotal":5,"cycleCharge":200}]}
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
         * total : 2
         * page : 1
         * rows : [{"phone":"18205253808","stationNo":"181139437395","createTime":1493174835000,"stationType":2,"userType ":0,"userName":"","type":1,"hourTotal":5,"cycleCharge":200},{"phone":"18205253808","stationNo":"181139437395","createTime":1493173707000,"stationType":2,"userType":0,"userName":"","type":2,"hourTotal":5,"cycleCharge":200}]
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
             * phone : 18205253808
             * stationNo : 181139437395
             * createTime : 1493174835000
             * stationType : 2
             * userType  : 0
             * userName :
             * type : 1
             * hourTotal : 5
             * cycleCharge : 200
             * userType : 0
             */

            private String phone;
            private String stationNo;
            private long createTime;
            private int stationType;
            private int userType;
            private String userName;
            private int type;
            private int hourTotal;
            private int cycleCharge;


            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getStationNo() {
                return stationNo;
            }

            public void setStationNo(String stationNo) {
                this.stationNo = stationNo;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getStationType() {
                return stationType;
            }

            public void setStationType(int stationType) {
                this.stationType = stationType;
            }

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getHourTotal() {
                return hourTotal;
            }

            public void setHourTotal(int hourTotal) {
                this.hourTotal = hourTotal;
            }

            public int getCycleCharge() {
                return cycleCharge;
            }

            public void setCycleCharge(int cycleCharge) {
                this.cycleCharge = cycleCharge;
            }


        }
    }
}
