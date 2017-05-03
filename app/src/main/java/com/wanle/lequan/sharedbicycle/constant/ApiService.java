package com.wanle.lequan.sharedbicycle.constant;

import com.wanle.lequan.sharedbicycle.bean.IdentityVerifyBean;
import com.wanle.lequan.sharedbicycle.bean.UserInfoBean;
import com.wanle.lequan.sharedbicycle.bean.VerificationCode;
import com.wanle.lequan.sharedbicycle.bean.loginBean;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * autor:Jerry
 * fuction:项目中使用到的接口
 * Date: 2017/2/24.
 */

public interface ApiService {
    public static final String BASE_URL = "http://test.car.lequangroup.cn/";
    public String testurl = "http://192.168.0.103:8080/car/";

    //获取验证码的接口
    @GET("smsCode/getSmsCodeByReg.html")
    Observable<VerificationCode> getVerificationCode(@Query("phone") String phone);

    //登录的接口
    @POST("user/login.html")
    Observable<loginBean> getLoginInfo(@QueryMap Map<String, String> map);

    //获取用户信息的接口
    @POST("user/personalCenter.html")
    Observable<UserInfoBean> getUserInfo(@QueryMap Map<String, String> map);

    //实名认证的接口
    @POST("user/userVerified.html")
    Observable<IdentityVerifyBean> identity_verify(@QueryMap Map<String, String> map);

    //行程列表的接口
    @FormUrlEncoded
    @POST("itinerary/userItineraryList.html")
    Call<ResponseBody> getTripList(@FieldMap Map<String, String> map);

    //获取服务端返回的用户信息json对象
    @FormUrlEncoded
    @POST("user/personalCenter.html")
    Call<ResponseBody> getUserinfoString(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("user/userVerified.html")
    Call<ResponseBody> getIdentifyString(@FieldMap Map<String, String> map);

    //查找附近车接口
    @GET("car/searchCar.html")
    Call<ResponseBody> queryCar(@QueryMap Map<String, String> map);

    //查找附近充电站点的接口
    @GET("place/selectPlaceList.html")
    Call<ResponseBody> queryChargeStation(@QueryMap Map<String, String> map);

    //充值接口
    @FormUrlEncoded
    @POST("recharge/rechargeCreate.html")
    Call<ResponseBody> recharge(@FieldMap Map<String, String> map);

    //意见反馈接口
    @FormUrlEncoded
    @POST("feedback/userFeedback.html")
    Call<ResponseBody> feedBack(@FieldMap Map<String, String> map);

    //头像上传的接口
    @Multipart
    @POST("user/uploadHeadImg.html")
    Call<ResponseBody> UploadIco(@Query("userId") String description, @Part("uploadFile\"; filename=\"test.jpg\"") RequestBody imgs);

    //行程明细的接口
    @FormUrlEncoded
    @POST("itinerary/userItineraryRecord.html")
    Call<ResponseBody> getTripDetail(@FieldMap Map<String, String> map);

    //充值记录的接口
    @FormUrlEncoded
    @POST("recharge/rechargeRecord.html")
    Call<ResponseBody> rechargeRecord(@FieldMap Map<String, String> map);

    //我要用车的接口
    @FormUrlEncoded
    @POST("car/useCar.html")
    Call<ResponseBody> userCar(@FieldMap Map<String, String> map);

    //临时锁车的接口
    @FormUrlEncoded
    @POST("car/temporaryLockCar.html")
    Call<ResponseBody> tempLock(@Field("userId") String userId);

    //还车校验的接口
    @FormUrlEncoded
    @POST("car/checkIfPark.html")
    Call<ResponseBody> returnCheck(@FieldMap Map<String, String> map);

    //我要还车的接口
    @FormUrlEncoded
    @POST("car/returnCar.html")
    Call<ResponseBody> returnCar(@FieldMap Map<String, String> map);

    //查询当前用车状态的接口
    @FormUrlEncoded
    @POST("car/searchUseCarStatus.html")
    Call<ResponseBody> carStuts(@Field("userId") String userId);

    //临时锁车后继续使用的接口
    @FormUrlEncoded
    @POST("car/unLockCar.html")
    Call<ResponseBody> continueUse(@Field("userId") String userId);

    //校验车辆状态的接口
    @FormUrlEncoded
    @POST("car/useCarCheckStatus.html")
    Call<ResponseBody> checkCarState(@FieldMap Map<String, String> map);

    //申请退还押金的接口
    @FormUrlEncoded
    @POST("recharge/rechargeBack.html")
    Call<ResponseBody> depositRefund(@Field("userId") String userId);

    //获得全局参数的接口
    @FormUrlEncoded
    @POST("user/note.html")
    Call<ResponseBody> globalParms(@Field("userId") String userId);

    //单车租还充电宝的接口
    @FormUrlEncoded
    @POST("powerBank/usePowerBank.html")
    Call<ResponseBody> leaseReturnCdb(@FieldMap Map<String, String> map);

    //租还充电宝记录的接口
    @FormUrlEncoded
    @POST("user/powerBank/powerBankRecord.html")
    Call<ResponseBody> leaseReturnCdbRecord(@FieldMap Map<String, String> map);

    //单车借还车辆电池的接口
    @FormUrlEncoded
    @POST("carBattery/useBattery.html")
    Call<ResponseBody> replaceReturnBattery(@FieldMap Map<String, String> map);

    //借还出车辆电池记录的接口
    @FormUrlEncoded
    @POST("user/powerBank/powerBankRecord.html")
    Call<ResponseBody> replaceReturnBatteryRecord(@FieldMap Map<String, String> map);
}
