package com.android.common;

import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;

import java.util.Locale;

/**
 * Created by guohao4 on 2017/8/8.
 * Email: Tornaco@163.com
 */

public class Locales {

    public static boolean isEn() {
        Configuration configuration = InstrumentationRegistry.getTargetContext()
                .getResources().getConfiguration();
        return configuration.locale == Locale.ENGLISH
                || configuration.locale == Locale.US
                || configuration.locale == Locale.UK
                || configuration.locale == Locale.CANADA;
    }
}
