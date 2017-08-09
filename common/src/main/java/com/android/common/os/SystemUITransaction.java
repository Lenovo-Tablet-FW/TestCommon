package com.android.common.os;

/**
 * Created by Nick on 2017/6/9 13:33
 */

public abstract class SystemUITransaction {

    private SystemUIControl control;

    public SystemUITransaction(SystemUIControl control) {
        this.control = control;
    }

    public SystemUIControl control() {
        return control;
    }

    public abstract SystemUITransaction applyChecked();

    public abstract SystemUITransaction apply();
}
