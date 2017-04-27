package com.wanle.lequan.sharedbicycle.bean;

import java.io.Serializable;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/11.
 */

public class CarStateCheckBean implements Serializable{

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"carResult":{"powerCount":0,"maxPowerCount":"1","minCarPower":"10","batteryCount":0,"maxBatteryCount":"2"},"powerBank":{"powerBankStatus":0},"battery":{"batteryNo":"2017042116261079819744","distance":100000,"carPower":100,"batteryStatus":1,"version":"1.0"}}
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

    public static class ResponseObjBean implements Serializable{
        /**
         * carResult : {"powerCount":0,"maxPowerCount":"1","minCarPower":"10","batteryCount":0,"maxBatteryCount":"2"}
         * powerBank : {"powerBankStatus":0}
         * battery : {"batteryNo":"2017042116261079819744","distance":100000,"carPower":100,"batteryStatus":1,"version":"1.0"}
         */

        private CarResultBean carResult;
        private PowerBankBean powerBank;
        private BatteryBean battery;

        public CarResultBean getCarResult() {
            return carResult;
        }

        public void setCarResult(CarResultBean carResult) {
            this.carResult = carResult;
        }

        public PowerBankBean getPowerBank() {
            return powerBank;
        }

        public void setPowerBank(PowerBankBean powerBank) {
            this.powerBank = powerBank;
        }

        public BatteryBean getBattery() {
            return battery;
        }

        public void setBattery(BatteryBean battery) {
            this.battery = battery;
        }

        public static class CarResultBean implements Serializable{
            /**
             * powerCount : 0
             * maxPowerCount : 1
             * minCarPower : 10
             * batteryCount : 0
             * maxBatteryCount : 2
             */

            private int powerCount;
            private String maxPowerCount;
            private String minCarPower;
            private int batteryCount;
            private String maxBatteryCount;

            public int getPowerCount() {
                return powerCount;
            }

            public void setPowerCount(int powerCount) {
                this.powerCount = powerCount;
            }

            public String getMaxPowerCount() {
                return maxPowerCount;
            }

            public void setMaxPowerCount(String maxPowerCount) {
                this.maxPowerCount = maxPowerCount;
            }

            public String getMinCarPower() {
                return minCarPower;
            }

            public void setMinCarPower(String minCarPower) {
                this.minCarPower = minCarPower;
            }

            public int getBatteryCount() {
                return batteryCount;
            }

            public void setBatteryCount(int batteryCount) {
                this.batteryCount = batteryCount;
            }

            public String getMaxBatteryCount() {
                return maxBatteryCount;
            }

            public void setMaxBatteryCount(String maxBatteryCount) {
                this.maxBatteryCount = maxBatteryCount;
            }
        }

        public static class PowerBankBean implements Serializable{
            /**
             * powerBankStatus : 0
             */

            private int powerBankStatus;

            public int getPowerBankStatus() {
                return powerBankStatus;
            }

            public void setPowerBankStatus(int powerBankStatus) {
                this.powerBankStatus = powerBankStatus;
            }
        }

        public static class BatteryBean implements Serializable{
            /**
             * batteryNo : 2017042116261079819744
             * distance : 100000
             * carPower : 100
             * batteryStatus : 1
             * version : 1.0
             */

            private String batteryNo;
            private int distance;
            private int carPower;
            private int batteryStatus;
            private String version;

            public String getBatteryNo() {
                return batteryNo;
            }

            public void setBatteryNo(String batteryNo) {
                this.batteryNo = batteryNo;
            }

            public int getDistance() {
                return distance;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }

            public int getCarPower() {
                return carPower;
            }

            public void setCarPower(int carPower) {
                this.carPower = carPower;
            }

            public int getBatteryStatus() {
                return batteryStatus;
            }

            public void setBatteryStatus(int batteryStatus) {
                this.batteryStatus = batteryStatus;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }
        }
    }
}
