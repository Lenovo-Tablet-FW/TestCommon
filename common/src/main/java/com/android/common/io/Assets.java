package com.android.common.io;

import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;

import com.android.common.io.Files;

import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by guohao4 on 2017/8/7.
 * Email: Tornaco@163.com
 */
@SuppressWarnings("ConstantConditions")
public class Assets {

    public static void copyFromAssetsTo(String from, String to) throws IOException {

        if (!new File(to).getParentFile().exists()) {
            Files.createParentDirsInShell(new File(to));
        }

        // 1. Copy to tmp. --> Java
        // 2. Copy to dest.--> Shell
        // 3. Remove tmp. -- Shell

        File tmp = new File(InstrumentationRegistry
                .getTargetContext()
                .getExternalCacheDir().getPath()
                + File.separator + from);
        Files.createParentDirsInShell(tmp);

        Assert.assertTrue(com.google.common.io.Files.asByteSink(tmp).writeFrom(openInput(from)) > 0);
        Files.copyInShell(tmp.getAbsolutePath(), to);

        // Remove tmp.
        Files.deleteInShell(tmp.getPath());
    }

    public static InputStream openInput(String fileName) throws IOException {
        return openAsset().open(fileName);
    }

    public static AssetManager openAsset() {
        return InstrumentationRegistry.getTargetContext().getAssets();
    }
}
