package com.android.common;

import org.junit.Assert;

import java.lang.reflect.Method;

/**
 * Created by guohao4 on 2017/8/8.
 * Email: Tornaco@163.com
 */

public class SystemProperties {

    public static String get(String key) throws ClassNotFoundException {
        Class sysPropClz = Class.forName("android.os.SystemProperties");
        Method get = ReflectionUtils.findMethod(sysPropClz, "get", String.class);
        Assert.assertNotNull(get);
        get.setAccessible(true);
        Object res = ReflectionUtils.invokeMethod(get, sysPropClz, key);
        return String.valueOf(res);
    }
}
