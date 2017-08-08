package com.android.common;

import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.Switch;

import org.junit.Assert;
import org.newstand.logger.Logger;

/**
 * Created by Nick on 2017/6/9 13:20
 */

public class SystemUITransactionHelper implements SystemUIControl {

    public static final long NEW_WINDOW_WAIT_TIMEOUT = 5 * 1000;
    public static final long UI_OBJECT_WAIT_TIMEOUT = 3 * 100;

    private UiDevice mDevice;

    public SystemUITransactionHelper() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Override
    public SystemUITransaction home() {
        return new Home(this);
    }

    @Override
    public SystemUITransaction back() {
        return new Back(this);
    }

    @Override
    public SystemUITransaction menu() {
        return new Menu(this);
    }

    @Override
    public SystemUITransaction openNotification() {
        return new OpenNotification(this);
    }

    @Override
    public SystemUITransaction openQuickSettings() {
        return new OpenQuickSettings(this);
    }

    @Override
    public SystemUITransaction closeQuickSettings() {
        return new CloseQuickSettings(this);
    }

    @Override
    public SystemUITransaction sleep() {
        return new BaseTransaction(this) {
            @Override
            Res execute() {
                try {
                    mDevice.sleep();
                    return Res.ok();
                } catch (RemoteException e) {
                    return Res.fail(e);
                }
            }
        };
    }

    @Override
    public SystemUITransaction wakeUp() {
        return new BaseTransaction(this) {
            @Override
            Res execute() {
                try {
                    mDevice.wakeUp();
                } catch (RemoteException e) {
                    return Res.fail(e);
                }
                return Res.ok();
            }
        };
    }

    @Override
    public SystemUITransaction unLock() {
        return new BaseTransaction(this) {
            @Override
            Res execute() {
                UiObject o = mDevice.findObject(new UiSelector().resourceId("com.android.systemui:id/notification_stack_scroller"));
                o.waitForExists(UI_OBJECT_WAIT_TIMEOUT);
                try {
                    return Res.from(o.swipeUp(10));
                } catch (UiObjectNotFoundException e) {
                    return Res.fail(e);
                }
            }
        };
    }

    @Override
    public SystemUITransaction addTile(final StatusBarToggle t) {
        return new BaseTransaction(this) {
            @Override
            Res execute() {
                boolean exists = false;
                // Check if exists.
                switch (t) {
                    case DolbyProfile:
                        // Find UI Object.
                        String[] maybe = {"DOLBY OFF", "Movie", "Music", "Game", "Voice"};
                        for (String aMaybe : maybe) {
                            UiObject btn = mDevice.findObject(new UiSelector()
                                    .textContains(aMaybe));
                            if (btn.waitForExists(UI_OBJECT_WAIT_TIMEOUT)) {
                                exists = true;
                                break;
                            }
                        }
                        break;

                    default:
                        // Find UI Object.
                        UiObject btn = mDevice.findObject(new UiSelector()
                                .descriptionContains(t.getDesc()));
                        exists = btn.waitForExists(UI_OBJECT_WAIT_TIMEOUT);
                        break;
                }

                if (exists) {
                    Logger.i("Toggle %s already exist.", t.name());
                    return Res.ok();
                }

                // Try add this tile.
                // 1. Find edit btn.
                UiObject editBtn = mDevice.findObject(new UiSelector().resourceId("com.android.systemui:id/editor_button"));
                if (!editBtn.exists() && !editBtn.waitForExists(UI_OBJECT_WAIT_TIMEOUT)) {
                    return Res.fail("Could not find edit button.");
                }
                try {
                    editBtn.clickAndWaitForNewWindow(NEW_WINDOW_WAIT_TIMEOUT);
                } catch (UiObjectNotFoundException e) {
                    return Res.fail(e);
                }

                // 2. Find target tile.
                UiObject tileView = null;
                switch (t) {
                    case DolbyProfile:
                        // Find UI Object.
                        String[] maybe = {"DOLBY OFF", "Movie", "Music", "Game", "Voice"};
                        for (String s : maybe) {
                            tileView = mDevice.findObject(new UiSelector()
                                    .textContains(s));
                            if (tileView.waitForExists(UI_OBJECT_WAIT_TIMEOUT)) {
                                exists = true;
                                break;
                            }
                        }
                        break;

                    default:
                        // Find UI Object.
                        tileView = mDevice.findObject(new UiSelector()
                                .descriptionContains(t.getDesc()));
                        exists = tileView.waitForExists(UI_OBJECT_WAIT_TIMEOUT);
                        break;
                }

                if (!exists) {
                    return Res.fail("Could not find tile:" + t);
                }

                // 3. Drag toggle.
                try {
                    return Res.from(mDevice.drag(
                            tileView.getBounds().centerX(),
                            tileView.getBounds().centerY(),
                            0,
                            0,
                            1000));
                } catch (UiObjectNotFoundException e) {
                    return Res.fail(e);
                } finally {
                    mDevice.pressBack();
                }
            }
        };
    }

    @Override
    public SystemUITransaction toggle(StatusBarToggle toggle, boolean on) {
        return new Toggle(this, toggle, on);
    }

    @Override
    public SystemUITransaction longClickToggle(StatusBarToggle toggle) {
        return new LongClickToggle(this, toggle);
    }

    @Override
    public SystemUITransaction threadSleep(long timeMills) {
        Sleep s = new Sleep(this);
        s.timeMills = timeMills;
        return s;
    }

    @Override
    public SystemUITransaction clearNotifications() {
        return new ClearNotification(this);
    }

    @Override
    public SystemUITransaction clickNotification(String notificationTextMatch) {
        return new ClickNotification(this, notificationTextMatch);
    }

    @Override
    public SystemUITransaction waitForNotification(String notificationTextMatch, long timeout) {
        return new WaitForNotification(this, notificationTextMatch, timeout);
    }

    @Override
    public SystemUITransaction expandNotification(String pkg, long timeout) {
        return new ExpandNotification(this, pkg, timeout);
    }

    private class ExpandNotification extends BaseTransaction {

        private long timeout;
        private String packageName;

        public ExpandNotification(SystemUIControl control, String packageName, long timeout) {
            super(control);
            this.packageName = packageName;
            this.timeout = timeout;
        }

        @Override
        Res execute() {

            openNotification().applyChecked();
            try {
                UiObject exp = mDevice.findObject(new UiSelector()
                        .packageName(packageName)
                        .resourceId("android:id/expand_button"));
                exp.waitForExists(timeout);
                return Res.from(exp.click());
            } catch (UiObjectNotFoundException e) {
                return Res.fail(e);
            }
        }
    }

    private class WaitForNotification extends BaseTransaction {

        private String textMatcher;
        private long timeout;

        public WaitForNotification(SystemUIControl control, String textMatcher, long timeout) {
            super(control);
            this.textMatcher = textMatcher;
            this.timeout = timeout;
        }

        @Override
        Res execute() {

            openNotification().applyChecked();
            UiObject object = mDevice.findObject(new UiSelector().textContains(textMatcher));
            return Res.from(object.waitForExists(timeout));
        }
    }

    private class ClickNotification extends BaseTransaction {

        private String textMatcher;

        public ClickNotification(SystemUIControl control, String textMatcher) {
            super(control);
            this.textMatcher = textMatcher;
        }

        @Override
        Res execute() {

            openNotification().applyChecked();
            UiObject object = mDevice.findObject(new UiSelector().textContains(textMatcher));
            object.waitForExists(UI_OBJECT_WAIT_TIMEOUT);

            try {
                return Res.from(object.click());
            } catch (UiObjectNotFoundException e) {
                return Res.fail(e);
            }
        }
    }

    private class ClearNotification extends BaseTransaction {

        ClearNotification(SystemUIControl control) {
            super(control);
        }

        @Override
        Res execute() {

            openNotification().applyChecked();
            UiObject clearObj = mDevice.findObject(new UiSelector().text("CLEAR ALL"));
            clearObj.waitForExists(UI_OBJECT_WAIT_TIMEOUT);

            try {
                return Res.from(clearObj.click());
            } catch (UiObjectNotFoundException e) {
                return Res.fail(e);
            } finally {
                mDevice.pressBack();
            }
        }
    }

    private class LongClickToggle extends Toggle {

        LongClickToggle(SystemUIControl control, StatusBarToggle t) {
            super(control, t, false);
        }

        @Override
        Res execute() {
            switch (t) {
                case DolbyProfile:
                    try {
                        // Find UI Object.
                        String[] maybe = {"DOLBY OFF", "Movie", "Music", "Game", "Voice"};
                        for (String aMaybe : maybe) {
                            UiObject btn = mDevice.findObject(new UiSelector()
                                    .textContains(aMaybe));
                            if (btn.waitForExists(UI_OBJECT_WAIT_TIMEOUT)) {

                                // Mock a long click within 1000ms.
                                if (!mDevice.swipe(btn.getBounds().centerX(),
                                        btn.getBounds().centerY(),
                                        btn.getBounds().centerX(),
                                        btn.getBounds().centerY(),
                                        1000)) {
                                    return Res.fail("Fail long click");
                                }

//                                if (!btn.longClick()) {
//                                    return Res.fail("Fail long click");
//                                }
                                break;
                            }
                        }
                    } catch (UiObjectNotFoundException e) {
                        return Res.fail(e);
                    }
                    return Res.ok();
                default:
                    String name = t.getDesc();
                    UiObject obj = mDevice.findObject(new UiSelector()
                            .descriptionContains(name));
                    obj.waitForExists(UI_OBJECT_WAIT_TIMEOUT);
                    try {
                        // Mock a long click within 1000ms.
                        if (!mDevice.swipe(obj.getBounds().centerX(),
                                obj.getBounds().centerY(),
                                obj.getBounds().centerX(),
                                obj.getBounds().centerY(),
                                1000)) {
                            return Res.fail("Fail long click");
                        }
                    } catch (UiObjectNotFoundException e) {
                        return Res.fail(e);
                    }
                    return Res.ok();
            }
        }
    }

    private class Toggle extends BaseTransaction {

        StatusBarToggle t;
        boolean on;

        Toggle(SystemUIControl control, StatusBarToggle t, boolean on) {
            super(control);
            this.t = t;
            this.on = on;
        }

        @Override
        Res execute() {
            switch (t) {
                case GPS:
                case APM:
                    try {
                        // Find UI Object.
                        UiObject btn = mDevice.findObject(new UiSelector()
                                .descriptionContains(t.getDesc()));
                        btn.waitForExists(UI_OBJECT_WAIT_TIMEOUT);
                        btn.click();
                    } catch (UiObjectNotFoundException e) {
                        return Res.fail(e);
                    }
                    return Res.ok();

                case DolbyProfile:
                    try {
                        // Find UI Object.
                        String[] maybe = {"DOLBY OFF", "Movie", "Music", "Game", "Voice"};
                        for (String aMaybe : maybe) {
                            UiObject btn = mDevice.findObject(new UiSelector()
                                    .textContains(aMaybe));
                            if (btn.waitForExists(UI_OBJECT_WAIT_TIMEOUT)) {
                                btn.click();

                                if (!on) {
                                    UiObject switcher = mDevice.findObject(new UiSelector()
                                            .className(Switch.class));
                                    switcher.waitForExists(UI_OBJECT_WAIT_TIMEOUT);
                                    switcher.click();
                                }

                                break;
                            }
                        }
                    } catch (UiObjectNotFoundException e) {
                        return Res.fail(e);
                    }
                    return Res.ok();

                default:
                    try {
                        // Find UI Object.
                        UiObject btn = mDevice.findObject(new UiSelector()
                                .descriptionContains(t.getDesc()));
                        btn.waitForExists(UI_OBJECT_WAIT_TIMEOUT);
                        btn.click();

                        if (!on) {
                            UiObject switcher = mDevice.findObject(new UiSelector()
                                    .className(Switch.class));
                            switcher.waitForExists(UI_OBJECT_WAIT_TIMEOUT);
                            switcher.click();
                        }

                    } catch (UiObjectNotFoundException e) {
                        return Res.fail(e);
                    }
                    return Res.ok();
            }

        }

        @Override
        String name() {
            return toString();
        }

        @Override
        public String toString() {
            return "Toggle{" +
                    "t=" + t +
                    ", on=" + on +
                    '}';
        }
    }

    private class Home extends BaseTransaction {

        Home(SystemUIControl control) {
            super(control);
        }

        @Override
        Res execute() {
            return Res.from(mDevice.pressHome());
        }
    }

    private class Back extends BaseTransaction {

        Back(SystemUIControl control) {
            super(control);
        }

        @Override
        Res execute() {
            return Res.from(mDevice.pressBack());
        }
    }

    private class Menu extends BaseTransaction {

        Menu(SystemUIControl control) {
            super(control);
        }

        @Override
        Res execute() {
            return Res.from(mDevice.pressMenu());
        }
    }

    private class Sleep extends BaseTransaction {

        long timeMills;

        Sleep(SystemUIControl control) {
            super(control);
        }

        @Override
        Res execute() {
            Assert.assertTrue("Sleep time should large than 0", timeMills > 0);
            try {
                Thread.sleep(timeMills);
            } catch (InterruptedException e) {
                return Res.fail(e);
            }
            return Res.ok();
        }
    }

    private class OpenNotification extends BaseTransaction {

        OpenNotification(SystemUIControl control) {
            super(control);
        }

        @Override
        Res execute() {
            if (mDevice.openNotification()) {
                return Res.ok();
            }
            return Res.fail();
        }
    }

    private class OpenQuickSettings extends BaseTransaction {

        OpenQuickSettings(SystemUIControl control) {
            super(control);
        }

        @Override
        Res execute() {
            if (mDevice.openQuickSettings()) {
                return Res.ok();
            }
            return Res.fail();
        }
    }

    private class CloseQuickSettings extends BaseTransaction {

        CloseQuickSettings(SystemUIControl control) {
            super(control);
        }

        @Override
        Res execute() {
            if (mDevice.pressBack()) {
                return Res.ok();
            }
            return Res.fail();
        }
    }

    abstract class BaseTransaction extends SystemUITransaction {

        BaseTransaction(SystemUIControl control) {
            super(control);
        }

        @Override
        public SystemUITransaction applyChecked() {
            Res r = execute();
            Assert.assertTrue("Fail execute:" + name() + "\n" + r.msg, r.ok);
            return this;
        }

        @Override
        public SystemUITransaction apply() {
            execute();
            return this;
        }

        abstract Res execute();

        String name() {
            return getClass().getSimpleName();
        }
    }

    static class Res {

        boolean ok;
        String msg;

        static Res ok() {
            return new Res(true, null);
        }

        static Res fail() {
            return fail(new Throwable());
        }

        static Res fail(String msg) {
            return fail(new Throwable(msg));
        }

        static Res fail(Throwable e) {
            return new Res(false, formatStackTrace(e));
        }

        static Res from(boolean res) {
            return new Res(res, "---");
        }

        public Res(boolean ok, String msg) {
            this.ok = ok;
            this.msg = msg;
        }

        private static String formatStackTrace(Throwable e) {
            StringBuilder sb = new StringBuilder();
            sb.append("-----STACK TRACE BEGIN-----").append("\n");
            StackTraceElement[] se = e.getStackTrace();
            for (StackTraceElement s : se) {
                sb.append(s.toString()).append("\n");
            }
            sb.append("-----STACK TRACE END-----").append("\n");
            return sb.toString();
        }
    }
}
