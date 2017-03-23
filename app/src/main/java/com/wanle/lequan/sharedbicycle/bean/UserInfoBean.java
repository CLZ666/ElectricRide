package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/2/28.
 */

public class UserInfoBean {


    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"balance":0,"certificateNo":"","code":"","createTime":null,"freeQuota":0,"headImg":"","id":"ec349541dbfe4ead90afc8b78bbe7c9b","integral":null,"isFree":0,"isVerified":1,"payDeposit":0,"payPassword":"","phone":"18855091326","reasonRemark":"","rechargeMoney":null,"recommendCode":"","status":null,"updateTime":null,"userName":"é\u0099\u0088è\u0089¯ç\u009f¥"}
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
         * balance : 0
         * certificateNo :
         * code :
         * createTime : null
         * freeQuota : 0
         * headImg :
         * id : ec349541dbfe4ead90afc8b78bbe7c9b
         * integral : null
         * isFree : 0
         * isVerified : 1
         * payDeposit : 0
         * payPassword :
         * phone : 18855091326
         * reasonRemark :
         * rechargeMoney : null
         * recommendCode :
         * status : null
         * updateTime : null
         * userName : éè¯ç¥
         */

        private int balance;
        private String certificateNo;
        private String code;
        private Object createTime;
        private int freeQuota;
        private String headImg;
        private String id;
        private Object integral;
        private int isFree;
        private int isVerified;
        private int payDeposit;
        private String payPassword;
        private String phone;
        private String reasonRemark;
        private Object rechargeMoney;
        private String recommendCode;
        private Object status;
        private Object updateTime;
        private String userName;

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
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

        public int getFreeQuota() {
            return freeQuota;
        }

        public void setFreeQuota(int freeQuota) {
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

        public int getIsFree() {
            return isFree;
        }

        public void setIsFree(int isFree) {
            this.isFree = isFree;
        }

        public int getIsVerified() {
            return isVerified;
        }

        public void setIsVerified(int isVerified) {
            this.isVerified = isVerified;
        }

        public int getPayDeposit() {
            return payDeposit;
        }

        public void setPayDeposit(int payDeposit) {
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

        @Override
        public String toString() {
            return "ResponseObjBean{" +
                    "balance=" + balance +
                    ", certificateNo='" + certificateNo + '\'' +
                    ", code='" + code + '\'' +
                    ", createTime=" + createTime +
                    ", freeQuota=" + freeQuota +
                    ", headImg='" + headImg + '\'' +
                    ", id='" + id + '\'' +
                    ", integral=" + integral +
                    ", isFree=" + isFree +
                    ", isVerified=" + isVerified +
                    ", payDeposit=" + payDeposit +
                    ", payPassword='" + payPassword + '\'' +
                    ", phone='" + phone + '\'' +
                    ", reasonRemark='" + reasonRemark + '\'' +
                    ", rechargeMoney=" + rechargeMoney +
                    ", recommendCode='" + recommendCode + '\'' +
                    ", status=" + status +
                    ", updateTime=" + updateTime +
                    ", userName='" + userName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", responseObj=" + responseObj +
                '}';
    }
}
