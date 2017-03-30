package com.wanle.lequan.sharedbicycle.utils;

import android.content.Context;

import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleGattCallback;
import com.clj.fastble.scan.ListScanCallback;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/3/29.
 */

public class BlueToothControl {
    private BleManager mBleManager;

    public BlueToothControl(Context context) {
        mBleManager = new BleManager(context);
    }
    public BleManager getBleManager(){
        if (null!=mBleManager){
            return mBleManager;
        }
        return null;
    }
    /**
     * 判断设备是否支持蓝牙
     */
    public boolean isSupportBle() {
        return mBleManager.isSupportBle();
    }

    /**
     * 扫描附近蓝牙设备
     */
    public void scanDevice(ListScanCallback listScanCallback) {
        mBleManager.scanDevice(listScanCallback);
    }

    /**
     * 根据蓝牙设备的名字去连接
     */
    public void connectByName(String deviceName, Long timeOut, BleGattCallback callback) {
        mBleManager.scanNameAndConnect(deviceName,timeOut,true,callback);
    }
    /**
     * 根据蓝牙设备的MAC地址去连接
     */
    public void connectByMac(String mac, Long timeOut, BleGattCallback callback){
        mBleManager.scanMacAndConnect(mac,timeOut,true,callback);
    }
    /**
     * 断开此次蓝牙连接
     */
    public void disConnect(){
        mBleManager.closeBluetoothGatt();
    }
}
