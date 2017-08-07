package com.android.common;

/**
 * Created by Nick on 2017/6/9 13:50
 */

public enum StatusBarToggle {

    WLAN("WLAN"),
    BT("Bluetooth"),
    APM("Airplane mode"),
    AutoRotate("Auto-rotate"),
    DolbyProfile(""),
    Screenshot("Screenshot"),
    GPS("Location");

    private String desc;

    StatusBarToggle(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
