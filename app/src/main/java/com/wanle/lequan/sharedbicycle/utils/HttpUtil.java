package com.wanle.lequan.sharedbicycle.utils;

import com.wanle.lequan.sharedbicycle.constant.ApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * autor:Jerry
 * fuction:网络请求工具类
 * Date: 2017/2/24.
 */

public class HttpUtil {
    private static HttpUtil instance;
    private Retrofit mRetrofit;
    private HttpUtil(){
        this.instance=this;
        this.mRetrofit=new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    private static HttpUtil getInstance(){
        if (instance == null) {
            synchronized (HttpUtil.class) {
                if (instance == null) {
                    return new HttpUtil();
                }
            }
        }
        return instance;
    }
    public static <T> T getService(Class<T> c) {
        return getInstance().mRetrofit.create(c);
    }

    public static <T> void init(Observable<T> observable, Subscriber<T> subscriber) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
