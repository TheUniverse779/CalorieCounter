package com.caloriecounter.calorie.ui.charging.iinterface;

public interface BatteryCallback {
    void OnBatteryConnected();
    void OnBatteryDisconnected();
    void OnBatteryChanged();
}
