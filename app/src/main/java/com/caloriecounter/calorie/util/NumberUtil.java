package com.caloriecounter.calorie.util;

public class NumberUtil {
    public static String round(String d){
        Double aDouble = Double.parseDouble(d);
        int i = (int) Math.round(aDouble);
        return String.valueOf(i);
    }

    public static String roundToFirst(double number){
        String result = null;
        try {
            result = String.format("%.1f", number);
            if(result.endsWith("0")){
                return result.substring(0, result.length() - 2);
            }else {
                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}
