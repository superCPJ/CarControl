package com.example.carcontrol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class BluetoothController {
    private BluetoothAdapter mAdapter;
    public BluetoothController(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }
    //判断当前设备是否支持蓝牙
    public boolean isSupportBlueTooth(){
        if (mAdapter != null){
            return true;
        }else {
            return false;
        }
    }
    public BluetoothAdapter getAdapter(){
        return mAdapter;
    }
    //判断当前蓝牙状态
    public boolean getBluetoothStatus(){
        assert (mAdapter != null);
        return mAdapter.isEnabled();
    }
    //打开蓝牙
    public void turnOnBluetooth(Activity activity, int requestCode){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent,requestCode);
    }
    //关闭蓝牙
    public void turnOffBluetooth(){
        mAdapter.disable();
    }
    //搜索设备
    public void findDevice(){
        assert (mAdapter != null);
        mAdapter.startDiscovery();
    }
    //查看绑定设备
    public List<BluetoothDevice> getBondedDeviceList(){
        return new ArrayList<>(mAdapter.getBondedDevices());
    }
    //打开可见
    public void enableVisibly(Activity activity,int requestCode){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        activity.startActivity(discoverableIntent);
    }
}
