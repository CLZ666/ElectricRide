package com.wanle.lequan.sharedbicycle.bean;


// FIXME generate failure  field _$Rows324

import java.util.List;

/**
 * autor:Jerry
 * fuction
 * Date: 2017/3/9.
 */

public class RechargeRecordBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"total":57,"page":6,"rows":[{"income":0,"pay":200,"ctime":1492053364000,"type":4},{"income":1000,"pay":0,"ctime":1492052986000,"type":7},{"income":1,"pay":0,"ctime":1492052986000,"type":2},{"income":0,"pay":200,"ctime":1491975267000,"type":4},{"income":0,"pay":200,"ctime":1491969949000,"type":4},{"income":0,"pay":200,"ctime":1491968222000,"type":4},{"income":1,"pay":0,"ctime":1491967307000,"type":1}]}
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
         * total : 57
         * page : 6
         * rows : [{"income":0,"pay":200,"ctime":1492053364000,"type":4},{"income":1000,"pay":0,"ctime":1492052986000,"type":7},{"income":1,"pay":0,"ctime":1492052986000,"type":2},{"income":0,"pay":200,"ctime":1491975267000,"type":4},{"income":0,"pay":200,"ctime":1491969949000,"type":4},{"income":0,"pay":200,"ctime":1491968222000,"type":4},{"income":1,"pay":0,"ctime":1491967307000,"type":1}]
         */

        private int total;
        private int page;
        private List<RowsBean> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * income : 0
             * pay : 200
             * ctime : 1492053364000
             * type : 4
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
