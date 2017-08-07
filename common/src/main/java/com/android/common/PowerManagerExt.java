package com.android.common;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import org.junit.Assert;
import org.newstand.logger.Logger;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by guohao4 on 2017/7/10.
 */

public enum PowerManagerExt {
    Instance;

    public int getBatteryLevel() throws IOException {
        String cmd = "dumpsys battery";
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        String res = device.executeShellCommand(cmd);
        Logger.d("getPreviousBatteryLevel, res:%s", res);
        StringTokenizer tokenizer = new StringTokenizer(res, "\n");
        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            if (token.contains("level")) {
                String level = token.replace("level: ", "").trim();
                Logger.d("level str:%s", level);
                return Integer.parseInt(level);
            }
        }
        throw new IllegalArgumentException("Could not get level by dumpsys");
    }

    public void setBatteryLevel(int level) throws IOException {
        Assert.assertTrue(level >= 0 && level <= 100);
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        String res = device.executeShellCommand(String.format(" dumpsys battery set level %s", level));
        Logger.d("setPreviousBatteryLevel, res:%s", res);
    }

    public static PowerManagerExt getInstance() {
        return Instance;
    }
}
