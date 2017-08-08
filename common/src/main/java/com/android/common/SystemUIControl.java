package com.android.common;

/**
 * Created by Nick on 2017/6/9 13:31
 */

public interface SystemUIControl {
    // Basic control
    SystemUITransaction home();

    SystemUITransaction back();

    SystemUITransaction menu();

    SystemUITransaction openQuickSettings();

    SystemUITransaction openNotification();

    SystemUITransaction closeQuickSettings();

    SystemUITransaction sleep();

    SystemUITransaction wakeUp();

    SystemUITransaction unLock();

    SystemUITransaction addTile(StatusBarToggle toggle);

    SystemUITransaction toggle(StatusBarToggle toggle, boolean on);

    SystemUITransaction longClickToggle(StatusBarToggle toggle);

    SystemUITransaction threadSleep(long timeMills);

    SystemUITransaction clearNotifications();

    SystemUITransaction clickNotification(String notificationTextMatch);

    SystemUITransaction waitForNotification(String notificationTextMatch, long timeout);

    SystemUITransaction expandNotification(String notificationTextMatch, long timeout);
}
