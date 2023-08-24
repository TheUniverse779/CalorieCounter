package com.caloriecounter.calorie.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public static String timeStampToDateTimeFormat(long timeStamp, String dateFormat){
        SimpleDateFormat sdfHM =  new SimpleDateFormat(dateFormat, Locale.getDefault());
        return sdfHM.format(new Date(timeStamp*1000));
    }

    public static String timeStampMillisecondToDateTimeFormat(long timeStamp, String dateFormat){
        SimpleDateFormat sdfHM =  new SimpleDateFormat(dateFormat, Locale.getDefault());
        return sdfHM.format(new Date(timeStamp));
    }
}
