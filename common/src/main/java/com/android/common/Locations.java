package com.android.common;

/**
 * Created by guohao4 on 2017/8/8.
 * Email: Tornaco@163.com
 */

public class Locations {

    @Deprecated
    public static void setEnabled(boolean enabled) {
        String cmd =
                enabled ?
                        String.format("settings put secure location_providers_allowed %s", "network,gps")
                        : String.format("settings put secure location_providers_allowed %s", "network,gps");
    }
}
