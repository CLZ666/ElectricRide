package com.wanle.lequan.sharedbicycle.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wanle.lequan.sharedbicycle.R;


/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/13.
 */

public class ProgersssDialog extends Dialog {


    TextView mIdTvLoadingmsg;


    public ProgersssDialog(Context context) {
        super(context, R.style.progress_dialog);

        //加载布局文件
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.progress_dialog, null);
        mIdTvLoadingmsg= (TextView) view.findViewById(R.id.id_tv_loadingmsg);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        //给图片添加动态效果
        //Animation anim = AnimationUtils.loadAnimation(context, R.anim.loading_dialog_progressbar);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //dialog添加视图
        setContentView(view);
        show();
//           dismiss();
    }

    public void setMsg(String msg) {
        mIdTvLoadingmsg.setText(msg);
    }

    public void setMsg(int msgId) {
        mIdTvLoadingmsg.setText(msgId);
    }

}
