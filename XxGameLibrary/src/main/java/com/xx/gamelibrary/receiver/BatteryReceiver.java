package com.xx.gamelibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xx.gamelibrary.callback.OnBatteryChangeListener;

/**
 * BatteryReceiver
 * (。・∀・)ノ
 * Describe：电量广播接收者
 * Created by 雷小星🍀 on 2017/7/26 11:14.
 */

public class BatteryReceiver extends BroadcastReceiver {
    private OnBatteryChangeListener batteryChangeListener;

    public BatteryReceiver(OnBatteryChangeListener batteryChangeListener) {
        this.batteryChangeListener = batteryChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int current = intent.getExtras().getInt("level");//获得当前电量
        int total = intent.getExtras().getInt("scale");//获得总电量
        int percent = current * 100 / total;
        batteryChangeListener.onBatteryChange(String.valueOf(percent));
    }
}
