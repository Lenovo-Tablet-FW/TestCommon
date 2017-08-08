package com.android.common;

import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;

/**
 * Created by guohao4 on 2017/8/8.
 * Email: Tornaco@163.com
 */

public class Locales {

    public static boolean isEn() {
        Configuration configuration = InstrumentationRegistry.getTargetContext()
                .getResources().getConfiguration();
        return configuration.locale.toString().contains("en");
    }
}
