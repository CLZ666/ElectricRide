package com.wanle.lequan.sharedbicycle.bean;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/4.
 */

public class ZfbOrderInfoBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseObj : {"orderSign":"_input_charset=utf-8&app_id=&appenv=&body=乐圈国际馆&extern_token=&goods_type=1&hb_fq_param=&it_b_pay=&notify_url=http://test.car.lequangroup.cn:80/recharge/alipayNotify.html&out_trade_no=20170304203051245471&partner=2088311999407139&payment_type=1&rn_check=F&seller_id=2088311999407139&service=mobile.securitypay.pay&sign=B2mG7OaowdCecJqJJXHM7R9ntxSyzvUHQqwlJ9ULafzeYmPlC8bXXpArYCSgk9AHmiVy1d5EhAHzh%2F2Kr5ifcCnlsFtDD3JeHSCl9ZcBBrM%2BuQAuXdbzlR5Z8NeIvpQKdted9JzGa0daEZ%2FzXQms70DxndaEBlcdmtobQCOF5es%3D&sign_type=RSA&subject=乐圈国际馆&total_fee=0.1"}
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
         * orderSign : _input_charset=utf-8&app_id=&appenv=&body=乐圈国际馆&extern_token=&goods_type=1&hb_fq_param=&it_b_pay=&notify_url=http://test.car.lequangroup.cn:80/recharge/alipayNotify.html&out_trade_no=20170304203051245471&partner=2088311999407139&payment_type=1&rn_check=F&seller_id=2088311999407139&service=mobile.securitypay.pay&sign=B2mG7OaowdCecJqJJXHM7R9ntxSyzvUHQqwlJ9ULafzeYmPlC8bXXpArYCSgk9AHmiVy1d5EhAHzh%2F2Kr5ifcCnlsFtDD3JeHSCl9ZcBBrM%2BuQAuXdbzlR5Z8NeIvpQKdted9JzGa0daEZ%2FzXQms70DxndaEBlcdmtobQCOF5es%3D&sign_type=RSA&subject=乐圈国际馆&total_fee=0.1
         */

        private String orderSign;

        public String getOrderSign() {
            return orderSign;
        }

        public void setOrderSign(String orderSign) {
            this.orderSign = orderSign;
        }

        @Override
        public String toString() {
            return "ResponseObjBean{" +
                    "orderSign='" + orderSign + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ZfbOrderInfoBean{" +
                "responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", responseObj=" + responseObj +
                '}';
    }
}
