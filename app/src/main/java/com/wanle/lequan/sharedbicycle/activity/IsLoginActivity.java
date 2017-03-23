package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.view.MyScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class IsLoginActivity extends AppCompatActivity {
    private static final int START_ALPHA = 0;
    private static final int END_ALPHA = 255;
    @BindView(R.id.user_icon)
    CircleImageView mUserIcon;
    @BindView(R.id.activity_is_login)
    RelativeLayout mActivityIsLogin;
    private int fadingHeight = 250;
    private Drawable mDrawable;
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
        setContentView(R.layout.activity_is_login);
        ButterKnife.bind(this);
        mDrawable = getResources().getDrawable(R.drawable.color_exam_grey);
        mDrawable.setAlpha(START_ALPHA);
        mLinear.setBackground(mDrawable);
        mScrollView.setOnScrollChangedListener(mListener);
    }

    /**
     * 公共接口：ScrollView的滚动监听
     */
    private MyScrollView.OnScrollChangedListener mListener = new MyScrollView.OnScrollChangedListener() {
        @Override
        public void onScrollChanged(ScrollView who, int x, int y, int oldx, int oldy) {
            if (y > fadingHeight) {
                mTvTitle.setVisibility(View.VISIBLE);
                y = fadingHeight;   //当滑动到指定位置之后设置颜色为纯色，之前的话要渐变---实现下面的公式即可
            } else {
                mTvTitle.setVisibility(View.INVISIBLE);
            }
            mDrawable.setAlpha(y * (END_ALPHA - START_ALPHA) / fadingHeight + START_ALPHA);
        }
    };


    @OnClick({R.id.btn_login_register, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_register:
                mDrawable.setAlpha(END_ALPHA);
                mLinear.setBackground(mDrawable);
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            case R.id.iv_back:
                mDrawable.setAlpha(END_ALPHA);
                mLinear.setBackground(mDrawable);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDrawable.setAlpha(END_ALPHA);
        mLinear.setBackground(mDrawable);
        finish();
    }
}
