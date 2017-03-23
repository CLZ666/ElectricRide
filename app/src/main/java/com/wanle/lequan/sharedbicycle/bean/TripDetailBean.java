package com.wanle.lequan.sharedbicycle.bean;

import java.util.List;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/9.
 */

public class TripDetailBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"itinerary":{"carId":"","carNo":"24929615696887808","cycleCharge":2,"cycleTime":7,"endTime":1487647852000,"id":"","itineraryJson":"","startTime":1487647430000,"status":null,"userId":""},"itineraryRecord":[{"latitude":"31.243239","longitude":"121.442931"},{"latitude":"31.243329","longitude":"121.442830"},{"latitude":"31.243129","longitude":"121.443830"}],"user":{"balance":null,"certificateNo":"","code":"","createTime":null,"freeQuota":null,"headImg":"","id":"","integral":null,"isFree":null,"isVerified":null,"payDeposit":null,"payPassword":"","phone":"18516024321","reasonRemark":"","rechargeMoney":null,"recommendCode":"","status":null,"updateTime":null,"userName":""}}
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
         * itinerary : {"carId":"","carNo":"24929615696887808","cycleCharge":2,"cycleTime":7,"endTime":1487647852000,"id":"","itineraryJson":"","startTime":1487647430000,"status":null,"userId":""}
         * itineraryRecord : [{"latitude":"31.243239","longitude":"121.442931"},{"latitude":"31.243329","longitude":"121.442830"},{"latitude":"31.243129","longitude":"121.443830"}]
         * user : {"balance":null,"certificateNo":"","code":"","createTime":null,"freeQuota":null,"headImg":"","id":"","integral":null,"isFree":null,"isVerified":null,"payDeposit":null,"payPassword":"","phone":"18516024321","reasonRemark":"","rechargeMoney":null,"recommendCode":"","status":null,"updateTime":null,"userName":""}
         */

        private ItineraryBean itinerary;
        private UserBean user;
        private List<ItineraryRecordBean> itineraryRecord;

        public ItineraryBean getItinerary() {
            return itinerary;
        }

        public void setItinerary(ItineraryBean itinerary) {
            this.itinerary = itinerary;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<ItineraryRecordBean> getItineraryRecord() {
            return itineraryRecord;
        }

        public void setItineraryRecord(List<ItineraryRecordBean> itineraryRecord) {
            this.itineraryRecord = itineraryRecord;
        }

        public static class ItineraryBean {
            /**
             * carId :
             * carNo : 24929615696887808
             * cycleCharge : 2
             * cycleTime : 7
             * endTime : 1487647852000
             * id :
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

        public static class UserBean {
            /**
             * balance : null
             * certificateNo :
             * code :
             * createTime : null
             * freeQuota : null
             * headImg :
             * id :
             * integral : null
             * isFree : null
             * isVerified : null
             * payDeposit : null
             * payPassword :
             * phone : 18516024321
             * reasonRemark :
             * rechargeMoney : null
             * recommendCode :
             * status : null
             * updateTime : null
             * userName :
             */

            private Object balance;
            private String certificateNo;
            private String code;
            private Object createTime;
            private Object freeQuota;
            private String headImg;
            private String id;
            private Object integral;
            private Object isFree;
            private Object isVerified;
            private Object payDeposit;
            private String payPassword;
            private String phone;
            private String reasonRemark;
            private Object rechargeMoney;
            private String recommendCode;
            private Object status;
            private Object updateTime;
            private String userName;

            public Object getBalance() {
                return balance;
            }

            public void setBalance(Object balance) {
                this.balance = balance;
            }

            public String getCertificateNo() {
                return certificateNo;
            }

            public void setCertificateNo(String certificateNo) {
                this.certificateNo = certificateNo;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public Object getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Object createTime) {
                this.createTime = createTime;
            }

            public Object getFreeQuota() {
                return freeQuota;
            }

            public void setFreeQuota(Object freeQuota) {
                this.freeQuota = freeQuota;
            }

            public String getHeadImg() {
                return headImg;
            }

            public void setHeadImg(String headImg) {
                this.headImg = headImg;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Object getIntegral() {
                return integral;
            }

            public void setIntegral(Object integral) {
                this.integral = integral;
            }

            public Object getIsFree() {
                return isFree;
            }

            public void setIsFree(Object isFree) {
                this.isFree = isFree;
            }

            public Object getIsVerified() {
                return isVerified;
            }

            public void setIsVerified(Object isVerified) {
                this.isVerified = isVerified;
            }

            public Object getPayDeposit() {
                return payDeposit;
            }

            public void setPayDeposit(Object payDeposit) {
                this.payDeposit = payDeposit;
            }

            public String getPayPassword() {
                return payPassword;
            }

            public void setPayPassword(String payPassword) {
                this.payPassword = payPassword;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getReasonRemark() {
                return reasonRemark;
            }

            public void setReasonRemark(String reasonRemark) {
                this.reasonRemark = reasonRemark;
            }

            public Object getRechargeMoney() {
                return rechargeMoney;
            }

            public void setRechargeMoney(Object rechargeMoney) {
                this.rechargeMoney = rechargeMoney;
            }

            public String getRecommendCode() {
                return recommendCode;
            }

            public void setRecommendCode(String recommendCode) {
                this.recommendCode = recommendCode;
            }

            public Object getStatus() {
                return status;
            }

            public void setStatus(Object status) {
                this.status = status;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
                this.updateTime = updateTime;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }

        public static class ItineraryRecordBean {
            /**
             * latitude : 31.243239
             * longitude : 121.442931
             */

            private String latitude;
            private String longitude;

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            @Override
            public String toString() {
                return "ItineraryRecordBean{" +
                        "latitude='" + latitude + '\'' +
                        ", longitude='" + longitude + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "TripDetailBean{" +
                "responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", responseObj=" + responseObj +
                '}';
    }
}
