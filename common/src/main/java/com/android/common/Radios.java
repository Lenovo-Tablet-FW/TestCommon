package com.android.common;

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
}
