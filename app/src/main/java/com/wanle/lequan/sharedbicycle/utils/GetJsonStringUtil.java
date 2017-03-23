package com.wanle.lequan.sharedbicycle.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/2.
 */

public class GetJsonStringUtil {
    public static void getJson_String( Call<ResponseBody> call,Callback<ResponseBody> callback){
        //Call<ResponseBody> call = HttpUtil.getService(ApiService.class).getUserinfoString(map);
        /*Callback<ResponseBody> identify *//*= new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null)
                    try {
                        Log.i("identify", response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        };*/
        call.enqueue(callback);
    /*    Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> call = apiService.getUserinfoString(userId);
        Log.i("userInfo",userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               *//* try {
                    Log.i("userInfo",response.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }*//*
                Log.i("userInfo",response.toString());
                Log.i("userInfo",call.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });*/
    }
}
