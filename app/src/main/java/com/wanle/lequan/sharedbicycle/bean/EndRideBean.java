package com.wanle.lequan.sharedbicycle.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/11.
 */

public class EndRideBean implements Parcelable {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"cycleCharge":200,"balance":2800,"timeTotal":2,"itineraryId":"aef00ef5a8954bba90c39874d52bfd9b"}
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

    public static class ResponseObjBean implements Parcelable {
        /**
         * cycleCharge : 200
         * balance : 2800
         * timeTotal : 2
         * itineraryId : aef00ef5a8954bba90c39874d52bfd9b
         */

        private int cycleCharge;
        private int balance;
        private int timeTotal;
        private String itineraryId;

        public int getCycleCharge() {
            return cycleCharge;
        }

        public void setCycleCharge(int cycleCharge) {
            this.cycleCharge = cycleCharge;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public int getTimeTotal() {
            return timeTotal;
        }

        public void setTimeTotal(int timeTotal) {
            this.timeTotal = timeTotal;
        }

        public String getItineraryId() {
            return itineraryId;
        }

        public void setItineraryId(String itineraryId) {
            this.itineraryId = itineraryId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.cycleCharge);
            dest.writeInt(this.balance);
            dest.writeInt(this.timeTotal);
            dest.writeString(this.itineraryId);
        }

        public ResponseObjBean() {
        }

        protected ResponseObjBean(Parcel in) {
            this.cycleCharge = in.readInt();
            this.balance = in.readInt();
            this.timeTotal = in.readInt();
            this.itineraryId = in.readString();
        }

        public static final Creator<ResponseObjBean> CREATOR = new Creator<ResponseObjBean>() {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.responseCode);
        dest.writeString(this.responseMsg);
        dest.writeParcelable(this.responseObj, flags);
    }

    public EndRideBean() {
    }

    protected EndRideBean(Parcel in) {
        this.responseCode = in.readString();
        this.responseMsg = in.readString();
        this.responseObj = in.readParcelable(ResponseObjBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<EndRideBean> CREATOR = new Parcelable.Creator<EndRideBean>() {
        @Override
        public EndRideBean createFromParcel(Parcel source) {
            return new EndRideBean(source);
        }

        @Override
        public EndRideBean[] newArray(int size) {
            return new EndRideBean[size];
        }
    };
}
