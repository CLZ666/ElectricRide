package com.wanle.lequan.sharedbicycle.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wanle.lequan.sharedbicycle.activity.BlueToothActivity;
import com.wanle.lequan.sharedbicycle.utils.AppManage;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/22.
 */

public class BlueToothStateReceiver extends BroadcastReceiver {

    private Context mContext;
    public BlueToothStateReceiver(Context context){
        this.mContext=context;
    }
    //监听蓝牙状态
    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
       // BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        //Toast.makeText(context, "蓝牙状态改变广播 !", Toast.LENGTH_LONG).show();
        switch (state){
            case BluetoothAdapter.STATE_TURNING_ON:
                break;
            case BluetoothAdapter.STATE_ON:
                AppManage.finishActivity(BlueToothActivity.class);
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                break;
            case BluetoothAdapter.STATE_OFF:
                mContext.startActivity(new Intent(mContext, BlueToothActivity.class));
                break;
            default:
                break;
        }

        Log.i("TAG---BlueTooth","接收到蓝牙状态改变广播！！");
       /* if(BluetoothDevice.ACTION_FOUND.equals(action))
        {
            Toast.makeText(context, device.getName() + " 设备已发现！！", Toast.LENGTH_LONG).show();
            btMessage=device.getName()+"设备已发现！！";
        }
        else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action))
        {
            Toast.makeText(context, device.getName() + "已连接", Toast.LENGTH_LONG).show();
            btMessage=device.getName()+"设备已连接！！";
        }

        else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action))
        {
            Toast.makeText(context, device.getName() + "正在断开蓝牙连接。。。", Toast.LENGTH_LONG).show();
            btMessage=device.getName()+"正在断开蓝牙连接。。。";
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action))
        {
            Toast.makeText(context, device.getName() + "蓝牙连接已断开！！！", Toast.LENGTH_LONG).show();
            btMessage=device.getName()+"蓝牙连接已断开！！";
        }*/
        /* if(action.equals("android.bluetooth.BluetoothAdapter.STATE_ON"))
        {
            Toast.makeText(context, "蓝牙已关闭", Toast.LENGTH_LONG).show();
            mContext.startActivity(new Intent(context, BlueToothActivity.class));
        }
        else if(action.equals("android.bluetooth.BluetoothAdapter.STATE_OFF"))
        {
            Toast.makeText(context, "蓝牙打开", Toast.LENGTH_LONG).show();
        }*/
        /*intent.putExtra("Bluetooth", btMessage);
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/
    }
}
