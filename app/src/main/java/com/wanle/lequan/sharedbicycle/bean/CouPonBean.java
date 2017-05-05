package com.wanle.lequan.sharedbicycle.bean;

import java.util.List;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/26.
 */

public class CouPonBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"total":1,"page":1,"rows":[{"wucId":"123","expireDays":5,"couponName":"红包","expireTime":1495123200000,"phone":"18205253808","createTime":1493781565000,"sourse":2,"userName":"李镇京","couponCode":"2017050309473769999555","discountMoney":"200"}]}
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
         * total : 1
         * page : 1
         * rows : [{"wucId":"123","expireDays":5,"couponName":"红包","expireTime":1495123200000,"phone":"18205253808","createTime":1493781565000,"sourse":2,"userName":"李镇京","couponCode":"2017050309473769999555","discountMoney":"200"}]
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
             * wucId : 123
             * expireDays : 5
             * couponName : 红包
             * expireTime : 1495123200000
             * phone : 18205253808
             * createTime : 1493781565000
             * sourse : 2
             * userName : 李镇京
             * couponCode : 2017050309473769999555
             * discountMoney : 200
             */

            private String wucId;
            private int expireDays;
            private String couponName;
            private long expireTime;
            private String phone;
            private long createTime;
            private int sourse;
            private String userName;
            private String couponCode;
            private String discountMoney;

            public String getWucId() {
                return wucId;
            }

            public void setWucId(String wucId) {
                this.wucId = wucId;
            }

            public int getExpireDays() {
                return expireDays;
            }

            public void setExpireDays(int expireDays) {
                this.expireDays = expireDays;
            }

            public String getCouponName() {
                return couponName;
            }

            public void setCouponName(String couponName) {
                this.couponName = couponName;
            }

            public long getExpireTime() {
                return expireTime;
            }

            public void setExpireTime(long expireTime) {
                this.expireTime = expireTime;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getSourse() {
                return sourse;
            }

            public void setSourse(int sourse) {
                this.sourse = sourse;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getCouponCode() {
                return couponCode;
            }

            public void setCouponCode(String couponCode) {
                this.couponCode = couponCode;
            }

            public String getDiscountMoney() {
                return discountMoney;
            }

            public void setDiscountMoney(String discountMoney) {
                this.discountMoney = discountMoney;
            }
        }
    }
}
