package com.wanle.lequan.sharedbicycle.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * autor:Jerry
 * fuction:附近的车的bean
 * Date: 2017/3/3.
 */

public class NearByCarBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : [{"carNo":"24929615696887809","carPower":99,"createTime":"","id":"","latitude":"31.243239","longitude":"121.442931","status":null,"useStatus":null,"version":""},{"carNo":"24929615696887808","carPower":100,"createTime":"","id":"","latitude":"31.243329","longitude":"121.442830","status":null,"useStatus":null,"version":""}]
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

    public static class ResponseObjBean implements Parcelable {
        /**
         * carNo : 24929615696887809
         * carPower : 99
         * createTime :
         * id :
         * latitude : 31.243239
         * longitude : 121.442931
         * status : null
         * useStatus : null
         * version :
         */

        private String carNo;
        private int carPower;
        private String createTime;
        private String id;
        private String latitude;
        private String longitude;
        private Object status;
        private Object useStatus;
        private String version;

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
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

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public Object getUseStatus() {
            return useStatus;
        }

        public void setUseStatus(Object useStatus) {
            this.useStatus = useStatus;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public String toString() {
            return "ResponseObjBean{" +
                    "carNo='" + carNo + '\'' +
                    ", carPower=" + carPower +
                    ", createTime='" + createTime + '\'' +
                    ", id='" + id + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", status=" + status +
                    ", useStatus=" + useStatus +
                    ", version='" + version + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.carNo);
            dest.writeInt(this.carPower);
            dest.writeString(this.createTime);
            dest.writeString(this.id);
            dest.writeString(this.latitude);
            dest.writeString(this.longitude);
            dest.writeString(this.version);
        }

        public ResponseObjBean() {
        }

        protected ResponseObjBean(Parcel in) {
            this.carNo = in.readString();
            this.carPower = in.readInt();
            this.createTime = in.readString();
            this.id = in.readString();
            this.latitude = in.readString();
            this.longitude = in.readString();
            this.status = in.readParcelable(Object.class.getClassLoader());
            this.useStatus = in.readParcelable(Object.class.getClassLoader());
            this.version = in.readString();
        }

        public static final Parcelable.Creator<ResponseObjBean> CREATOR = new Parcelable.Creator<ResponseObjBean>() {
            @Override
            public ResponseObjBean createFromParcel(Parcel source) {
                return new ResponseObjBean(source);
            }

            @Override
            public ResponseObjBean[] newArray(int size) {
                return new ResponseObjBean[size];
            }
        };
    }

    @Override
    public String toString() {
        return "NearByCarBean{" +
                "responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", responseObj=" + responseObj +
                '}';
    }
}
