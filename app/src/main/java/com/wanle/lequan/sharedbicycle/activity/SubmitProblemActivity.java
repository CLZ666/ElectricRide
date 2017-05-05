package com.wanle.lequan.sharedbicycle.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanle.lequan.sharedbicycle.MainActivity;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.EBikeProblemTypeAdapter;
import com.wanle.lequan.sharedbicycle.bean.MessageBean;
import com.wanle.lequan.sharedbicycle.constant.ApiService;
import com.wanle.lequan.sharedbicycle.event.MyEvent;
import com.wanle.lequan.sharedbicycle.utils.GetJsonStringUtil;
import com.wanle.lequan.sharedbicycle.utils.HttpUtil;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;
import com.wanle.lequan.sharedbicycle.utils.UpLoadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitProblemActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final int MY_PERMISSIONS_REQUEST_QR_CODE = 1;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rec_problem_type)
    RecyclerView mRecProblemType;
    @BindView(R.id.iv_add_image)
    ImageView mIvAddImage;
    @BindView(R.id.edit_car_no)
    EditText mEditCarNo;
    @BindView(R.id.edit_problem_desc)
    EditText mEditProblemDesc;
    private EBikeProblemTypeAdapter mAdapter;
    private String mProblemType = "";
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private View mView;
    private AlertDialog mDialog;
    private Bitmap mPhoto;
    private SharedPreferences mSpUserInfo;
    private int ptype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_problem);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("提交问题");
        mRecProblemType.setHasFixedSize(true);
        mRecProblemType.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new EBikeProblemTypeAdapter(this);
        mRecProblemType.setAdapter(mAdapter);
        mAdapter.setData(getData());
        mSpUserInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        EventBus.getDefault().register(this);
    }

    public List<String> getData() {
        List<String> data = new ArrayList<>();
        data.add("私锁占用");
        data.add("车辆刮花");
        data.add("车锁坏了");
        data.add("违规乱停");
        data.add("刹车坏了");
        data.add("轮胎坏了");
        data.add("其他");
        return data;
    }

    @Subscribe
    public void onEventMainThread(MyEvent event) {
        mProblemType = event.getMsg();
        switch (mProblemType) {
            case "私锁占用":
                ptype = 1;
                break;
            case "车辆刮花":
                ptype = 2;
                break;
            case "轮胎坏了":
                ptype = 3;
                break;
            case "车锁坏了":
                ptype = 4;
                break;
            case "违规乱停":
                ptype = 5;
                break;
            case "刹车坏了":
                ptype = 6;
                break;
            case "其它":
                ptype = 7;
                break;

        }
    }

    @OnClick({R.id.iv_back, R.id.iv_add_image, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add_image:
                showChoosePicDialog();
                break;
            case R.id.btn_submit:
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    submitProblem();
                }
                break;
        }
    }

    /**
     * 显示修改头像的对话框
     */
    private void showChoosePicDialog() {
        mView = LayoutInflater.from(this).inflate(R.layout.dialog_upload, null);
        mDialog = new AlertDialog.Builder(this).create();
        mDialog.setView(mView);
        mDialog.setCanceledOnTouchOutside(true);
        TextView tv_title = (TextView) mView.findViewById(R.id.tv_title);
        tv_title.setText("图片上传");
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
                default:
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
            mPhoto = extras.getParcelable("data");
            // photo = UpLoadUtils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            mIvAddImage.setImageBitmap(mPhoto);
            // Log.i("upload", new String(data1));
        }
    }

    private void submitProblem() {
        String userId = mSpUserInfo.getString("userId", "");
        final String carno = mEditCarNo.getText().toString().trim();
        final String problemDesc = mEditProblemDesc.getText().toString().trim();
        if (TextUtils.isEmpty(problemDesc) || TextUtils.isEmpty(carno)) {
            ToastUtil.show(this, "请确认所提交问题的信息完整！");
        } else if (TextUtils.isEmpty(mProblemType)) {
            ToastUtil.show(this, "请选择车辆问题类型");
        } else {
            if (null != mPhoto) {
                String imagePath = UpLoadUtils.savePhoto(mPhoto, Environment
                        .getExternalStorageDirectory().getAbsolutePath(), String
                        .valueOf(System.currentTimeMillis()));
                File file = new File(imagePath);
                RequestBody body = RequestBody.create(MediaType.parse("image/png"), file);
                final Call<ResponseBody> call = HttpUtil.getService(ApiService.class).submitProblem(userId, carno, ptype + "", problemDesc, body);
                GetJsonStringUtil.getJson_String(call, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if (response.code() == 200) {
                                String jsonString = response.body().string();
                                Log.i("problem1", jsonString);
                                if (null != jsonString) {
                                    Gson gson = new Gson();
                                    final MessageBean messageBean = gson.fromJson(jsonString, MessageBean.class);
                                    if (null != messageBean) {
                                        if (messageBean.getResponseCode().equals("1")) {
                                            ToastUtil.show(SubmitProblemActivity.this, "问题提交成功!");
                                            startActivity(new Intent(SubmitProblemActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            ToastUtil.show(SubmitProblemActivity.this, messageBean.getResponseMsg());
                                        }
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
            } else {
                ToastUtil.show(this, "请上传车辆照片");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(SubmitProblemActivity.this, "为了您能使用拍照上传功能,请打开照相机权限", R.string.confim, R.string.cancel, perms);
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
