package com.wanle.lequan.sharedbicycle.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.MainActivity;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.bean.VerificationCode;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtils;
import com.wanle.lequan.sharedbicycle.utils.UpLoadUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int MY_PERMISSIONS_REQUEST_QR_CODE = 1;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_is_identify)
    TextView mTvIsIdentify;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.iv_user_icon)
    CircleImageView mIvUserIcon;

    private SharedPreferences mMSp_userinfo;
    private boolean mIsIdentify;
    private String mUserName;
    private String mPhone;
    private SharedPreferences mIsLogin;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private View mView;
    private AlertDialog mDialog;
    private String mHeadimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("设置");
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mMSp_userinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        mIsLogin = getSharedPreferences("isLogin", MODE_PRIVATE);
        mIsIdentify = mMSp_userinfo.getBoolean(getResources().getString(R.string.is_identity), false);
        mUserName = mMSp_userinfo.getString("userName", "");
        mTvName.setText(mUserName);
        mPhone = mMSp_userinfo.getString("phone", "");
        if (mIsIdentify) {
            mTvIsIdentify.setText("已认证");
        } else {
            mTvIsIdentify.setText("未认证");
        }
        mTvPhone.setText(mPhone);
        mHeadimg = mMSp_userinfo.getString("headimg", "");
        Glide.with(this).load(mHeadimg).asBitmap().into(mIvUserIcon);
    }

    @OnClick({R.id.iv_back, R.id.rel_user_icon, R.id.rel_advice, R.id.rel_about_us, R.id.btn_exit_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rel_user_icon:
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    showChoosePicDialog();
                }
                break;
            case R.id.rel_advice:
                startActivity(new Intent(this, FeedBackActivity.class));
                break;
            case R.id.rel_about_us:
                break;
            case R.id.btn_exit_login:
                final AlertDialog dialog = new AlertDialog.Builder(this).create();
                View view1 = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
                dialog.setView(view1);
                dialog.setCanceledOnTouchOutside(true);
                TextView tv_confim = (TextView) view1.findViewById(R.id.tv_confim);
                TextView tv_cancel = (TextView) view1.findViewById(R.id.tv_cancel);
                tv_confim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIsLogin.edit().clear().commit();
                        mMSp_userinfo.edit().clear().commit();
                        startActivity(new Intent(SettingActivity.this, MainActivity.class));
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
        }
    }

    /**
     * 显示修改头像的对话框
     */
    private void showChoosePicDialog() {
        mView = LayoutInflater.from(this).inflate(R.layout.dialog_upload, null);
        mDialog = new AlertDialog.Builder(this).create();
       /* dialog.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        dialog.setNegativeButton("取消", null);*/
        mDialog.setView(mView);
        mDialog.setCanceledOnTouchOutside(true);
        TextView tv_choose = (TextView) mView.findViewById(R.id.tv_choose_picture);
        TextView tv_take = (TextView) mView.findViewById(R.id.tv_take_picture);
        TextView tv_cancel = (TextView) mView.findViewById(R.id.tv_cancel);
        tv_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAlbumIntent = new Intent(
                        Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                mDialog.cancel();
            }
        });
        tv_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPermission();
                mDialog.cancel();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        mDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            // photo = UpLoadUtils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            mIvUserIcon.setImageBitmap(photo);
            byte[] data1 = bitmap2Bytes(photo);
            Bitmap bitmap = Bytes2Bimap(data1);
            Log.i("upload", new String(data1));
            ToastUtil.show(this, "头像上传中");
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        String userId = mMSp_userinfo.getString("userId", "");
        Log.i("upload", userId);
        String imagePath = UpLoadUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        File file = new File(imagePath);

        if (bitmap != null) {
            Map<String, RequestBody> map = new HashMap<>();
            map.put("userId", toRequestBody(userId));
            Log.e("upload", imagePath);
            map.put("filename=\"" + file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            RequestBody body = RequestBody.create(MediaType.parse("image/png"), file);
            Call<ResponseBody> call = HttpUtil.getService(ApiService.class).UploadIco(userId, body);

            // Log.i("upload",map.get("userId").toString());
            GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Gson gson = new Gson();
                    try {
                        String jsonString = response.body().string();
                        if (null!=jsonString){
                            VerificationCode uploadBean = gson.fromJson(jsonString, VerificationCode.class);
                            if (uploadBean != null) {
                                String responseCode = uploadBean.getResponseCode();
                                if (responseCode.equals("1")) {
                                    ToastUtils.getShortToastByString(SettingActivity.this, "头像上传成功!");
                                } else {
                                    ToastUtils.getShortToastByString(SettingActivity.this, " 头像上传失败");
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public static RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse(" multipart/form-data"), value);
        return requestBody;
    }

    public byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
            EasyPermissions.checkDeniedPermissionsNeverAskAgain(SettingActivity.this, "为了您能使用拍照上传功能,请打开照相机权限", R.string.confim, R.string.cancel, perms);
    }

    @AfterPermissionGranted(MY_PERMISSIONS_REQUEST_QR_CODE)
    public void CameraPermission() {
        String[] params = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this.getApplicationContext(), params)) {
            Intent openCameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
            // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        } else {
            EasyPermissions.requestPermissions(this, "请求打开照相机权限!", MY_PERMISSIONS_REQUEST_QR_CODE, params);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
