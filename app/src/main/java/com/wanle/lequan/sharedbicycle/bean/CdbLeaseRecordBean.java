package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/21.
 */

public class CdbLeaseRecordBean{


        private int type;
        private int cdbNo;
        private String leaseTime;
        private String leaseSpend;

        public CdbLeaseRecordBean(int type, int cdbNo, String leaseTime, String leaseSpend) {
            this.type = type;
            this.cdbNo = cdbNo;
            this.leaseTime = leaseTime;
            this.leaseSpend = leaseSpend;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getCdbNo() {
            return cdbNo;
        }

        public void setCdbNo(int cdbNo) {
            this.cdbNo = cdbNo;
        }

        public String getLeaseTime() {
            return leaseTime;
        }

        public void setLeaseTime(String leaseTime) {
            this.leaseTime = leaseTime;
        }

        public String getLeaseSpend() {
            return leaseSpend;
        }

        public void setLeaseSpend(String leaseSpend) {
            this.leaseSpend = leaseSpend;
        }


}
