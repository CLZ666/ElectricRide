package com.wanle.lequan.sharedbicycle.utils;

import android.content.Context;
import android.util.Log;

import com.wanle.lequan.sharedbicycle.bean.UserInfoBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

import static android.content.Context.MODE_PRIVATE;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/2.
 */

public class GetUserInfo {
    public static void getUserInfo(Context context, Subscriber<UserInfoBean> subscriber){
        String userId = context.getSharedPreferences("userinfo", MODE_PRIVATE).getString("userId", "");

        // String userId = "99DBD039C7DCE2DA86889143946687EF6BD790241761D8CD8147EA299742DBCD6B2DC180FD294EC25F7DEBEB1F2B0DA7";
        Log.i("userinfo", userId);
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        Observable<UserInfoBean> observable = HttpUtil.getService(ApiService.class).getUserInfo(map);
        HttpUtil.init(observable, subscriber);
    }
}
