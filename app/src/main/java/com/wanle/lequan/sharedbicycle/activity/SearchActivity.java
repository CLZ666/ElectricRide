package com.wanle.lequan.sharedbicycle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.InputPromptAdapter;
import com.wanle.lequan.sharedbicycle.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchActivity extends AppCompatActivity implements Inputtips.InputtipsListener {

    @BindView(R.id.edit_search)
    EditText mEditSearch;
    InputtipsQuery mInputquery;
    @BindView(R.id.lv_tips)
    ListView mLvTips;
    private Inputtips mInputTips;

    private ArrayList<String> mTips;
    private InputPromptAdapter mAdapter;
    private String mKeyword;
    private static final int RESULT_CODE=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mEditSearch.addTextChangedListener(textWatcher);
        mAdapter = new InputPromptAdapter(this);
        mLvTips.setAdapter(mAdapter);
        setListener();
    }

    private void setListener() {
        mLvTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tip tip = (Tip) mAdapter.getItem(position);
                LatLonPoint point = tip.getPoint();
                if (point!=null){
                    Intent intent=new Intent();
                    intent.putExtra("point",point);
                    setResult(RESULT_CODE,intent);
                    SearchActivity.this.finish();
                }else{
                    ToastUtil.show(SearchActivity.this,"请输入详细地址");
                }
            }
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mKeyword = mEditSearch.getText().toString().trim();
            //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
            mInputquery = new InputtipsQuery(mKeyword, null);
            mInputTips = new Inputtips(SearchActivity.this, mInputquery);
            mInputTips.setInputtipsListener(SearchActivity.this);
            mInputTips.requestInputtipsAsyn();
            if (mKeyword.equals("")){
                mAdapter.clearData();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @OnClick(R.id.tv_cancel)
    public void onClick() {
        finish();
    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
       if (list!=null){
           mAdapter.clearData();
           mAdapter.setData(list);
       }
    }
}
