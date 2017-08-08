package com.android.common;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import junit.framework.Assert;

import org.newstand.logger.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Created by guohao4 on 2017/8/8.
 * Email: Tornaco@163.com
 */

@SuppressWarnings("ConstantConditions")
public class PackageInstaller {

    public static void installFromAssets(String name) throws IOException {
        String tmpPath = InstrumentationRegistry.getTargetContext()
                .getExternalCacheDir().getPath() + File.separator + "tmp.apk";
        Assets.copyFromAssetsTo(name, tmpPath);
        Logger.i("Installing with tmp apk: %s", tmpPath);
        String cmd = String.format("pm install -r %s", tmpPath);
        String res = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .executeShellCommand(cmd);
        Assert.assertTrue("Fail install package:" + name, res == null || !res.contains("Err"));
    }
}
