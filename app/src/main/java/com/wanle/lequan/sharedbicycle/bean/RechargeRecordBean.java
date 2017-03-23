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
     * responseObj : {"resultCode":1,"list":[{"income":1,"pay":0,"ctime":1488719491000,"type":1},{"income":1,"pay":0,"ctime":1488719231000,"type":1},{"income":1,"pay":0,"ctime":1488719110000,"type":1},{"income":1,"pay":0,"ctime":1488707554000,"type":2},{"income":1000,"pay":0,"ctime":1488707554000,"type":1},{"income":1,"pay":0,"ctime":1488637394000,"type":2},{"income":1000,"pay":0,"ctime":1488637394000,"type":1},{"income":1,"pay":0,"ctime":1488636714000,"type":2},{"income":1000,"pay":0,"ctime":1488636714000,"type":1},{"income":1,"pay":0,"ctime":1488633396000,"type":2},{"income":1000,"pay":0,"ctime":1488633396000,"type":1},{"income":1,"pay":0,"ctime":1488632839000,"type":2},{"income":1000,"pay":0,"ctime":1488632839000,"type":1},{"income":1,"pay":0,"ctime":1488632569000,"type":2},{"income":1000,"pay":0,"ctime":1488632569000,"type":1},{"income":10,"pay":0,"ctime":1488632228000,"type":2},{"income":1000,"pay":0,"ctime":1488632228000,"type":1}]}
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
         * list : [{"income":1,"pay":0,"ctime":1488719491000,"type":1},{"income":1,"pay":0,"ctime":1488719231000,"type":1},{"income":1,"pay":0,"ctime":1488719110000,"type":1},{"income":1,"pay":0,"ctime":1488707554000,"type":2},{"income":1000,"pay":0,"ctime":1488707554000,"type":1},{"income":1,"pay":0,"ctime":1488637394000,"type":2},{"income":1000,"pay":0,"ctime":1488637394000,"type":1},{"income":1,"pay":0,"ctime":1488636714000,"type":2},{"income":1000,"pay":0,"ctime":1488636714000,"type":1},{"income":1,"pay":0,"ctime":1488633396000,"type":2},{"income":1000,"pay":0,"ctime":1488633396000,"type":1},{"income":1,"pay":0,"ctime":1488632839000,"type":2},{"income":1000,"pay":0,"ctime":1488632839000,"type":1},{"income":1,"pay":0,"ctime":1488632569000,"type":2},{"income":1000,"pay":0,"ctime":1488632569000,"type":1},{"income":10,"pay":0,"ctime":1488632228000,"type":2},{"income":1000,"pay":0,"ctime":1488632228000,"type":1}]
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
             * income : 1
             * pay : 0
             * ctime : 1488719491000
             * type : 1
             */

            private int income;
            private int pay;
            private long ctime;
            private int type;

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

            public long getCtime() {
                return ctime;
            }

            public void setCtime(long ctime) {
                this.ctime = ctime;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
