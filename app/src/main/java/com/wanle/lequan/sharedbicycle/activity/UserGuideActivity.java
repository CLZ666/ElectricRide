package com.wanle.lequan.sharedbicycle.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserGuideActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.myProgressBar)
    ProgressBar mMyProgressBar;
    private SharedPreferences mSpGlobalParms;
    private View emptyView;
    private TextView mTv_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        ButterKnife.bind(this);
        NetWorkUtil.isNetworkAvalible(this);
        mSpGlobalParms = getSharedPreferences("global", MODE_PRIVATE);
        mTvTitle.setText("用户指南");
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        final String userGuide = mSpGlobalParms.getString("userGuide", "");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mMyProgressBar.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == mMyProgressBar.getVisibility()) {
                        mMyProgressBar.setVisibility(View.VISIBLE);
                    }
                    mMyProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        mWebView.loadUrl(userGuide);
        emptyView = findViewById(R.id.empty_view);
        mTv_empty = (TextView) emptyView.findViewById(R.id.tv_empty);
        netBug();
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetWorkUtil.isNetworkAvailable(UserGuideActivity.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mWebView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                    }, 1000);

                }
            }
        });
    }

    public void netBug() {
        if (!NetWorkUtil.isNetworkAvailable(this)) {
            mTv_empty.setText("网络连接失败，连接后点击刷新");
            emptyView.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
