package com.caloriecounter.calorie.util;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;


public class Util {
    public static void showToast(Context context, String mess) {
        if (mess != null) {
            Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
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


    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("sound_type.json");
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

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
