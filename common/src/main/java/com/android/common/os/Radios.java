package com.android.common.os;

import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import junit.framework.Assert;

import java.io.IOException;

/**
 * Created by guohao4 on 2017/8/8.
 * Email: Tornaco@163.com
 */

public class Radios {

    public static void setAPM(boolean on) throws IOException {
        String cmd = String.format("settings put global airplane_mode_on %s", on ? "1" : "0");
        String res = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).executeShellCommand(cmd);
        Assert.assertTrue("Fail toggle AMP mode", res == null || !res.contains("Err"));
    }

    public static boolean isAPM() {
        return Settings.System.getInt(InstrumentationRegistry.getTargetContext()
                        .getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }
}
