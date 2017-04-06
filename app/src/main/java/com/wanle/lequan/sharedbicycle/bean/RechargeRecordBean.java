package com.wanle.lequan.sharedbicycle.bean;

import java.util.List;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/9.
 */

public class RechargeRecordBean {


    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"resultCode":1,"list":[{"income":1000,"pay":0,"ctime":"2017-04-06 14:42:56","type":"7"},{"income":1,"pay":0,"ctime":"2017-04-06 14:42:56","type":"2"},{"income":1000,"pay":0,"ctime":"2017-03-16 12:33:56","type":"7"},{"income":1,"pay":0,"ctime":"2017-03-16 12:33:56","type":"2"}]}
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
         * resultCode : 1
         * list : [{"income":1000,"pay":0,"ctime":"2017-04-06 14:42:56","type":"7"},{"income":1,"pay":0,"ctime":"2017-04-06 14:42:56","type":"2"},{"income":1000,"pay":0,"ctime":"2017-03-16 12:33:56","type":"7"},{"income":1,"pay":0,"ctime":"2017-03-16 12:33:56","type":"2"}]
         */

        private int resultCode;
        private List<ListBean> list;

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * income : 1000
             * pay : 0
             * ctime : 2017-04-06 14:42:56
             * type : 7
             */

            private int income;
            private int pay;
            private String ctime;
            private String type;

            public int getIncome() {
                return income;
            }

            public void setIncome(int income) {
                this.income = income;
            }

            public int getPay() {
                return pay;
            }

            public void setPay(int pay) {
                this.pay = pay;
            }

            public String getCtime() {
                return ctime;
            }

            public void setCtime(String ctime) {
                this.ctime = ctime;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
