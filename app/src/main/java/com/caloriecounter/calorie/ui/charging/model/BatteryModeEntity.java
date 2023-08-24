package com.caloriecounter.calorie.ui.charging.model;

import android.content.Context;


import com.caloriecounter.calorie.R;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class BatteryModeEntity implements Serializable {
    private Boolean autoSync;
    private Boolean bluetooth;
    private String description;
    private Boolean hapticFeedback;
    private Integer id;
    private boolean isSystem;
    private String name;
    private Brightness screenBrightness;
    private ScreenLock screenLock;
    private SoundMode sound;
    private Boolean wifi;

    public BatteryModeEntity(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Brightness getScreenBrightness() {
        return this.screenBrightness;
    }

    public void setScreenBrightness(Brightness screenBrightness) {
        this.screenBrightness = screenBrightness;
    }

    public ScreenLock getScreenLock() {
        return this.screenLock;
    }

    public void setScreenLock(ScreenLock screenLock) {
        this.screenLock = screenLock;
    }

    public SoundMode getSound() {
        return this.sound;
    }

    public void setSound(SoundMode sound) {
        this.sound = sound;
    }

    public Boolean isWifi() {
        return this.wifi;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }

    public Boolean isBluetooth() {
        return this.bluetooth;
    }

    public void setBluetooth(Boolean bluetooth) {
        this.bluetooth = bluetooth;
    }

    public Boolean isAutoSync() {
        return this.autoSync;
    }

    public void setAutoSync(Boolean autoSync) {
        this.autoSync = autoSync;
    }

    public Boolean isHapticFeedback() {
        return this.hapticFeedback;
    }

    public void setHapticFeedback(Boolean hapticFeedback) {
        this.hapticFeedback = hapticFeedback;
    }

    public boolean isSystem() {
        return this.isSystem;
    }

    public enum Brightness {
        AUTO(0, R.string.brightness_auto, -1), PERCENT_10(1,
                R.string.brightness_10, 10), PERCENT_50(2,
                R.string.brightness_50, 50), PERCENT_80(3,
                R.string.brightness_80, 80), PERCENT_100(4,
                R.string.brightness_100, 100);

        private int id;
        private int nameId;
        private int value;

        private Brightness(int id, int nameId, int value) {
            this.id = id;
            this.nameId = nameId;
            this.value = value;
        }

        public static Brightness resolveId(int id) {
            if (id == AUTO.getId()) {
                return AUTO;
            }
            if (id == PERCENT_10.getId()) {
                return PERCENT_10;
            }
            if (id == PERCENT_50.getId()) {
                return PERCENT_50;
            }
            if (id == PERCENT_80.getId()) {
                return PERCENT_80;
            }
            if (id == PERCENT_100.getId()) {
                return PERCENT_100;
            }
            return null;
        }

        public int getId() {
            return this.id;
        }

        public String getName(Context context) {
            return context.getString(this.nameId);
        }

        public int getValue() {
            return this.value;
        }
    }


    public enum ScreenLock {
        SEC_15(TimeUnit.SECONDS.toMillis(15), R.string.screen_lock_15s), SEC_30(
                TimeUnit.SECONDS.toMillis(30), R.string.screen_lock_30s), MIN_01(
                TimeUnit.MINUTES.toMillis(1), R.string.screen_lock_1m), MIN_02(
                TimeUnit.MINUTES.toMillis(2), R.string.screen_lock_2m), MIN_10(
                TimeUnit.MINUTES.toMillis(10), R.string.screen_lock_10m), MIN_30(
                TimeUnit.MINUTES.toMillis(30), R.string.screen_lock_30m);

        private int nameId;
        private long value;

        private ScreenLock(long value, int nameId) {
            this.value = value;
            this.nameId = nameId;
        }

        public static ScreenLock resolveValue(int id) {
            if (((long) id) == SEC_15.getValue()) {
                return SEC_15;
            }
            if (((long) id) == SEC_30.getValue()) {
                return SEC_30;
            }
            if (((long) id) == MIN_01.getValue()) {
                return MIN_01;
            }
            if (((long) id) == MIN_02.getValue()) {
                return MIN_02;
            }
            if (((long) id) == MIN_10.getValue()) {
                return MIN_10;
            }
            if (((long) id) == MIN_30.getValue()) {
                return MIN_30;
            }
            return null;
        }

        public long getValue() {
            return this.value;
        }

        public String getName(Context context) {
            return context.getString(this.nameId);
        }
    }

    public enum SoundMode {
        SILENT(0, R.string.silent), VIBRATE(1, R.string.vibrate), ON(2,
                R.string.on);

        private int id;
        private int nameId;

        private SoundMode(int id, int nameId) {
            this.id = id;
            this.nameId = nameId;
        }

        public static SoundMode resolveId(int id) {
            if (id == ON.getId()) {
                return ON;
            }
            if (id == VIBRATE.getId()) {
                return VIBRATE;
            }
            if (id == SILENT.getId()) {
                return SILENT;
            }
            return null;
        }

        public int getId() {
            return this.id;
        }

        public String getName(Context context) {
            return context.getString(this.nameId);
        }
    }
}
