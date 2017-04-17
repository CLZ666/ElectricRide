package com.wanle.lequan.sharedbicycle.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/17.
 */

public class ReturnCheckBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"placeIn":[{"placeNo":"139","longLatiJson":[{"latitude":"30","longitude":"120"},{"latitude":"35","longitude":"123"},{"latitude":"29","longitude":"125"}],"placeName":"139号"}],"ifInStation":"YES"}
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
         * placeIn : [{"placeNo":"139","longLatiJson":[{"latitude":"30","longitude":"120"},{"latitude":"35","longitude":"123"},{"latitude":"29","longitude":"125"}],"placeName":"139号"}]
         * ifInStation : YES
         */

        private String ifInStation;
        private List<PlaceInBean> placeIn;

        public String getIfInStation() {
            return ifInStation;
        }

        public void setIfInStation(String ifInStation) {
            this.ifInStation = ifInStation;
        }

        public List<PlaceInBean> getPlaceIn() {
            return placeIn;
        }

        public void setPlaceIn(List<PlaceInBean> placeIn) {
            this.placeIn = placeIn;
        }

        public static class PlaceInBean implements Parcelable {
            /**
             * placeNo : 139
             * longLatiJson : [{"latitude":"30","longitude":"120"},{"latitude":"35","longitude":"123"},{"latitude":"29","longitude":"125"}]
             * placeName : 139号
             */

            private String placeNo;
            private String placeName;
            private List<LongLatiJsonBean> longLatiJson;

            public String getPlaceNo() {
                return placeNo;
            }

            public void setPlaceNo(String placeNo) {
                this.placeNo = placeNo;
            }

            public String getPlaceName() {
                return placeName;
            }

            public void setPlaceName(String placeName) {
                this.placeName = placeName;
            }

            public List<LongLatiJsonBean> getLongLatiJson() {
                return longLatiJson;
            }

            public void setLongLatiJson(List<LongLatiJsonBean> longLatiJson) {
                this.longLatiJson = longLatiJson;
            }

            public static class LongLatiJsonBean implements Parcelable {
                /**
                 * latitude : 30
                 * longitude : 120
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
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.latitude);
                    dest.writeString(this.longitude);
                }

                public LongLatiJsonBean() {
                }

                protected LongLatiJsonBean(Parcel in) {
                    this.latitude = in.readString();
                    this.longitude = in.readString();
                }

                public static final Creator<LongLatiJsonBean> CREATOR = new Creator<LongLatiJsonBean>() {
                    @Override
                    public LongLatiJsonBean createFromParcel(Parcel source) {
                        return new LongLatiJsonBean(source);
                    }

                    @Override
                    public LongLatiJsonBean[] newArray(int size) {
                        return new LongLatiJsonBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.placeNo);
                dest.writeString(this.placeName);
                dest.writeList(this.longLatiJson);
            }

            public PlaceInBean() {
            }

            protected PlaceInBean(Parcel in) {
                this.placeNo = in.readString();
                this.placeName = in.readString();
                this.longLatiJson = new ArrayList<LongLatiJsonBean>();
                in.readList(this.longLatiJson, LongLatiJsonBean.class.getClassLoader());
            }

            public static final Parcelable.Creator<PlaceInBean> CREATOR = new Parcelable.Creator<PlaceInBean>() {
                @Override
                public PlaceInBean createFromParcel(Parcel source) {
                    return new PlaceInBean(source);
                }

                @Override
                public PlaceInBean[] newArray(int size) {
                    return new PlaceInBean[size];
                }
            };
        }
    }
}
