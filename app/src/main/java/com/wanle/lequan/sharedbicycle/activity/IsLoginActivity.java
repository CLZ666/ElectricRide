package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.view.MyScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class IsLoginActivity extends BaseActivity {
    @BindView(R.id.user_icon)
    CircleImageView mUserIcon;

    @BindView(R.id.btn_login_register)
    Button mBtnLoginRegister;
    @BindView(R.id.linear)
    LinearLayout mLinear;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.scrollView)
    MyScrollView mScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_logged_in);
        ButterKnife.bind(this);
       // mScrollView.setOnScrollChangedListener(mListener);
    }



    @OnClick({R.id.btn_login_register, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_register:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
