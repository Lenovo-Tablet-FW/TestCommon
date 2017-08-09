package com.android.common.io;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import java.io.File;
import java.io.IOException;

/**
 * Created by guohao4 on 2017/8/7.
 * Email: Tornaco@163.com
 */

public class Files {

    public static void deleteInShell(String filePath) throws IOException {
        String cmd = String.format("rm -rf %s", filePath);
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .executeShellCommand(cmd);
        junit.framework.Assert.assertTrue("Fail remove:" + filePath, !new File(filePath).exists());
    }

    public static void copyInShell(String from, String to) throws IOException {
        String cmd = String.format("cp -rf %s %s", from, to);
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .executeShellCommand(cmd);
        junit.framework.Assert.assertTrue("Fail copy to:" + to, new File(to).exists());
    }

    public static void createParentDirsInShell(File file) throws IOException {
        File parent = file.getParentFile();
        String path = parent.getPath();
        String cmd = String.format("mkdir -p %s", path);
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .executeShellCommand(cmd);
        if (!parent.isDirectory()) {
            throw new IOException("Unable to create parent directories of " + file);
        }
    }
}
