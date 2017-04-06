package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/6.
 */

public class GlobalParmsBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"aboutUs":"安达市大所大所多啊实打实大说得跟干戈扰攘","customerService":"021-88888888","deposit":299,"userGuide":"http://www.baidu.com"}
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
         * aboutUs : 安达市大所大所多啊实打实大说得跟干戈扰攘
         * customerService : 021-88888888
         * deposit : 299
         * userGuide : http://www.baidu.com
         */

        private String aboutUs;
        private String customerService;
        private int deposit;
        private String userGuide;

        public String getAboutUs() {
            return aboutUs;
        }

        public void setAboutUs(String aboutUs) {
            this.aboutUs = aboutUs;
        }

        public String getCustomerService() {
            return customerService;
        }

        public void setCustomerService(String customerService) {
            this.customerService = customerService;
        }

        public int getDeposit() {
            return deposit;
        }

        public void setDeposit(int deposit) {
            this.deposit = deposit;
        }

        public String getUserGuide() {
            return userGuide;
        }

        public void setUserGuide(String userGuide) {
            this.userGuide = userGuide;
        }
    }
}
