package com.wanle.lequan.sharedbicycle.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
    private IWXAPI api;
    private static final String APP_ID = "wx1902de5450f7a61e";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_entry);
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        String errStr = baseResp.errStr;
        //Log.i("errstr",baseResp.errStr);
       // Log.i("errstr",baseResp.transaction);
        int errCode = baseResp.errCode;
        if (errCode==0){
            ToastUtils.getShortToastByString(this,"支付成功!");
            finish();
        }else if (errCode==-1){
            ToastUtils.getShortToastByString(this,"支付发生错误");
            finish();
        }else if (errCode==-2){
            ToastUtils.getShortToastByString(this,"操作已取消!");
        }
    }
}
