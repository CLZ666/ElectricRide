package com.wanle.lequan.sharedbicycle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;
import com.wanle.lequan.sharedbicycle.adapter.EBikeProblemTypeAdapter;
import com.wanle.lequan.sharedbicycle.event.MyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubmitProblemActivity extends AppCompatActivity {

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
    private String mProblemType="";
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
    }

    @OnClick({R.id.iv_back, R.id.iv_add_image, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add_image:
                break;
            case R.id.btn_submit:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
