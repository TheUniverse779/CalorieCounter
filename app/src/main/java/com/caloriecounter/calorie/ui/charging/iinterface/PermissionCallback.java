package com.caloriecounter.calorie.ui.charging.iinterface;

import android.app.Dialog;

public interface PermissionCallback {
    void OnSkip();
    void OnBack(Dialog dialog);
}
