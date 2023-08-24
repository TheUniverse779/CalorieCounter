package com.caloriecounter.calorie.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;


public class AppUtil {
    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    public static int mpsToKmph(double mps) {
        return (int) (3.6 * mps);
    }

    public static String mmToCm(String mm) {
        return String.valueOf(Double.parseDouble(mm) / 10.0);
    }

    public static String mmToIn(String mm) {
        return String.valueOf(Double.parseDouble(mm) / 25.4);
    }


    public static String hPaToBar(String hPaString) {
        double hPa = Double.parseDouble(hPaString);
        double bar = Math.round(hPa / 1000.0 * 10.0) / 10.0;
        return Double.toString(bar);
    }


    public static String hPaToAtm(String hPaString) {
        double hPa = Double.parseDouble(hPaString);
        double atm = Math.round(hPa / 1013.25 * 10.0) / 10.0;
        return Double.toString(atm);
    }

    public static String hPaToKPa(String hPaString) {
        double hPa = Double.parseDouble(hPaString);
        double kPa = Math.round(hPa * 0.1 * 10.0)/ 10.0;
        return Double.toString(kPa);
    }


    public static String hPaToMmHg(String hPaString) {
        double hPa = Double.parseDouble(hPaString);
        double mmHg = Math.round(hPa * 0.750061561303 * 10.0) / 10.0;
        return Double.toString(mmHg);
    }

    public static String hPaToInHg(String hPaString) {
        double hPa = Double.parseDouble(hPaString);
        double inHg = Math.round(hPa * 0.0295299830714 * 10.0) / 10.0;
        return Double.toString(inHg);
    }

    public static String hPaToPsi(String hPaString) {
        double hPa = Double.parseDouble(hPaString);
        double psi = Math.round(hPa * 0.00014503773773 * 10.0) / 10.0;
        return Double.toString(psi);
    }


    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getTextUVIndex(String uv){
        try {
            double uvIndex = Double.parseDouble(uv);
            if(uvIndex > 0 && uvIndex < 3){
                return uv + " (Low)";
            }else if(uvIndex >=3 && uvIndex < 6) {
                return uv + " (Moderate)";
            }else if(uvIndex >=6 && uvIndex < 8) {
                return uv + " (High)";
            }else if(uvIndex >=8 && uvIndex < 11) {
                return uv + " (Very high)";
            }else  {
                return uv + " (Extreme)";
            }
        } catch (Exception e) {
            return uv;
        }
    }

    public static String loadJSONFromAsset(Context activity, String name) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(name + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
