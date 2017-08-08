package com.android.common;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import junit.framework.Assert;

import java.io.IOException;

/**
 * Created by guohao4 on 2017/8/8.
 * Email: Tornaco@163.com
 */

public class Locations {

    public static void setGpsProviderEnabled(boolean enabled) throws IOException {
        String cmd =
                enabled ?
                        String.format("settings put secure location_providers_allowed %s", "+gps")
                        : String.format("settings put secure location_providers_allowed %s", "-gps");
        String res = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).executeShellCommand(cmd);
        Assert.assertTrue("Fail toggle gps", res == null || !res.contains("Err"));
    }
}
