package com.android.common;

import org.junit.Test;

/**
 * Created by guohao4 on 2017/8/8.
 * Email: Tornaco@163.com
 */
public class SystemUITransactionHelperTest {
    @Test
    public void addTile() throws Exception {
        new SystemUITransactionHelper().openQuickSettings()
                .applyChecked()
                .control()
                .addTile(StatusBarToggle.GPS)
                .applyChecked();
    }

}