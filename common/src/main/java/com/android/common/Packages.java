package com.android.common;

import android.content.pm.PackageManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import junit.framework.Assert;

import org.newstand.logger.Logger;

import java.io.IOException;

/**
 * Created by guohao4 on 2017/8/8.
 * Email: Tornaco@163.com
 */

public class Packages {

    public static void forceStop(String packageName) throws IOException {
        Assert.assertNotNull("Package name can not be null", packageName);
        Logger.i("forceStopping: %s", packageName);
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .executeShellCommand("am force-stop " + packageName);
    }

    public static boolean isInstalled(String packageName) throws PackageManager.NameNotFoundException {
        Object packageInfo = InstrumentationRegistry.getContext().getPackageManager().getPackageInfo(
                packageName, 0);
        return packageInfo != null;
    }
}
