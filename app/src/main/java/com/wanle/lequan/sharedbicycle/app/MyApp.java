package com.wanle.lequan.sharedbicycle.app;

import android.app.Application;
import android.util.Log;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/*import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;*/

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/1.
 */

public class MyApp extends Application {
    /* PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
     PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");*/
    @Override
    public void onCreate() {
        super.onCreate();
        Config.DEBUG = true;
        UMShareAPI.get(this);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.i("mytoken", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }
    {
        PlatformConfig.setWeixin("wxaee1318bd6e9e063","e4ea090a7fbe3b5d0edb6fa20873a44f");
    }

}
