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
     * responseObj : [{"createTime":null,"createUserId":"","id":"","latitude":"31.243429","longitude":"121.442930","placeAddress":"XX路128号","placeImg":"","placeName":"","placeNo":"128","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","latitude":"31.243329","longitude":"121.442830","placeAddress":"XX路129号","placeImg":"","placeName":"","placeNo":"129","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","latitude":"31.243529","longitude":"121.442730","placeAddress":"XX路130号","placeImg":"","placeName":"","placeNo":"130","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","latitude":"31.243629","longitude":"121.442630","placeAddress":"XX路131号","placeImg":"","placeName":"","placeNo":"131","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","latitude":"31.243729","longitude":"121.442530","placeAddress":"XX路132号","placeImg":"","placeName":"","placeNo":"132","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","latitude":"31.243829","longitude":"121.442430","placeAddress":"XX路133号","placeImg":"","placeName":"","placeNo":"133","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","latitude":"31.243929","longitude":"121.442330","placeAddress":"XX路134号","placeImg":"","placeName":"","placeNo":"134","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","latitude":"31.241329","longitude":"121.442230","placeAddress":"XX路135号","placeImg":"","placeName":"","placeNo":"135","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","latitude":"31.242329","longitude":"121.442130","placeAddress":"XX路136号","placeImg":"","placeName":"","placeNo":"136","status":null,"updateTime":null,"updateUserId":""},{"createTime":null,"createUserId":"","id":"","latitude":"31.243329","longitude":"121.441830","placeAddress":"XX路136号","placeImg":"","placeName":"","placeNo":"137","status":null,"updateTime":null,"updateUserId":""}]
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
         * latitude : 31.243429
         * longitude : 121.442930
         * placeAddress : XX路128号
         * placeImg :
         * placeName :
         * placeNo : 128
         * status : null
         * updateTime : null
         * updateUserId :
         */

        private Object createTime;
        private String createUserId;
        private String id;
        private String latitude;
        private String longitude;
        private String placeAddress;
        private String placeImg;
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

        @Override
        public String toString() {
            return "ResponseObjBean{" +
                    "createTime=" + createTime +
                    ", createUserId='" + createUserId + '\'' +
                    ", id='" + id + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", placeAddress='" + placeAddress + '\'' +
                    ", placeImg='" + placeImg + '\'' +
                    ", placeName='" + placeName + '\'' +
                    ", placeNo='" + placeNo + '\'' +
                    ", status=" + status +
                    ", updateTime=" + updateTime +
                    ", updateUserId='" + updateUserId + '\'' +
                    '}';
        }
    }
}
