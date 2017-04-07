package com.wanle.lequan.sharedbicycle.bean;

import java.util.List;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/7.
 */

public class NearByStationBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : [{"createTime":null,"createUserId":"","id":"","placeAddress":"XX路128号","placeImg":"","placeLatitude":"31.243429","placeLongitude":"121.442930","placeName":"128号","placeNo":"128","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","placeAddress":"XX路129号","placeImg":"","placeLatitude":"31.243329","placeLongitude":"121.442830","placeName":"129号","placeNo":"129","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","placeAddress":"XX路130号","placeImg":"","placeLatitude":"31.243529","placeLongitude":"121.442730","placeName":"130号","placeNo":"130","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","placeAddress":"XX路131号","placeImg":"","placeLatitude":"31.243629","placeLongitude":"121.442630","placeName":"131号","placeNo":"131","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","placeAddress":"XX路132号","placeImg":"","placeLatitude":"31.243729","placeLongitude":"121.442530","placeName":"132号","placeNo":"132","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","placeAddress":"XX路133号","placeImg":"","placeLatitude":"31.243829","placeLongitude":"121.442430","placeName":"133号","placeNo":"133","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","placeAddress":"XX路134号","placeImg":"","placeLatitude":"31.243929","placeLongitude":"121.442330","placeName":"134号","placeNo":"134","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","placeAddress":"XX路135号","placeImg":"","placeLatitude":"31.241329","placeLongitude":"121.442230","placeName":"135号","placeNo":"135","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","placeAddress":"XX路136号","placeImg":"","placeLatitude":"31.242329","placeLongitude":"121.442130","placeName":"136号","placeNo":"136","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","placeAddress":"XX路136号","placeImg":"","placeLatitude":"31.243329","placeLongitude":"121.441830","placeName":"137号","placeNo":"137","status":null,"updateTime":null,"updateUserId":""}]
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
         * createTime : null
         * createUserId :
         * id :
         * placeAddress : XX路128号
         * placeImg :
         * placeLatitude : 31.243429
         * placeLongitude : 121.442930
         * placeName : 128号
         * placeNo : 128
         * status : null
         * updateTime : null
         * updateUserId :
         */

        private Object createTime;
        private String createUserId;
        private String id;
        private String placeAddress;
        private String placeImg;
        private String placeLatitude;
        private String placeLongitude;
        private String placeName;
        private String placeNo;
        private Object status;
        private Object updateTime;
        private String updateUserId;

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public String getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(String createUserId) {
            this.createUserId = createUserId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlaceAddress() {
            return placeAddress;
        }

        public void setPlaceAddress(String placeAddress) {
            this.placeAddress = placeAddress;
        }

        public String getPlaceImg() {
            return placeImg;
        }

        public void setPlaceImg(String placeImg) {
            this.placeImg = placeImg;
        }

        public String getPlaceLatitude() {
            return placeLatitude;
        }

        public void setPlaceLatitude(String placeLatitude) {
            this.placeLatitude = placeLatitude;
        }

        public String getPlaceLongitude() {
            return placeLongitude;
        }

        public void setPlaceLongitude(String placeLongitude) {
            this.placeLongitude = placeLongitude;
        }

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }

        public String getPlaceNo() {
            return placeNo;
        }

        public void setPlaceNo(String placeNo) {
            this.placeNo = placeNo;
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

        public String getUpdateUserId() {
            return updateUserId;
        }

        public void setUpdateUserId(String updateUserId) {
            this.updateUserId = updateUserId;
        }
    }
}
