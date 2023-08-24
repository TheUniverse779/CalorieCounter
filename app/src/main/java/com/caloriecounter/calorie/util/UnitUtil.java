package com.caloriecounter.calorie.util;

import com.caloriecounter.calorie.Constant;

public class UnitUtil {
    public static String setPressure(PreferenceUtil preferenceUtil, String hPa){
        String unitResult = preferenceUtil.getValue(Constant.Setting.PRESSURE, Constant.SettingValue.PRESSURE_hPa);
        switch (unitResult){
            case Constant.SettingValue.PRESSURE_hPa:
                return hPa + " "+unitResult;
            case Constant.SettingValue.PRESSURE_mbar:
                return hPa + " "+unitResult;
            case Constant.SettingValue.PRESSURE_bar:
                return AppUtil.hPaToBar(hPa) + " "+unitResult;
            case Constant.SettingValue.PRESSURE_atm:
                return AppUtil.hPaToAtm(hPa) + " "+unitResult;
            case Constant.SettingValue.PRESSURE_kPa:
                return AppUtil.hPaToKPa(hPa) + " "+unitResult;
            case Constant.SettingValue.PRESSURE_mmHg:
                return AppUtil.hPaToMmHg(hPa) + " "+unitResult;
            case Constant.SettingValue.PRESSURE_inHg:
                return AppUtil.hPaToInHg(hPa) + " "+unitResult;
            case Constant.SettingValue.PRESSURE_psi:
                return AppUtil.hPaToPsi(hPa) + " "+unitResult;
        }
        return hPa + " "+unitResult;
    }
}
