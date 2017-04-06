package com.wanle.lequan.sharedbicycle.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.utils.NetWorkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserGuideActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.webView)
    WebView mWebView;
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
        mWebView.loadUrl(userGuide);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        emptyView = findViewById(R.id.empty_view);
        mTv_empty = (TextView) emptyView.findViewById(R.id.tv_empty);
        netBug();
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
