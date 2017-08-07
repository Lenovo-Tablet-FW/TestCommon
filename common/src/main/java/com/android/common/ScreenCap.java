package com.android.common;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import java.io.File;
import java.io.IOException;

/**
 * Created by guohao4 on 2017/7/12.
 */

public class ScreenCap {

    public static boolean takeScreenshot(String path) throws IOException {
        File file = new File(path);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            return false;
        }
        String cmd = String.format("screencap -p %s", path);
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .executeShellCommand(cmd);
        return true;
    }
}
