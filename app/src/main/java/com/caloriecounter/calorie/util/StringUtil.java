package com.caloriecounter.calorie.util;

public class StringUtil {
    public static String upperCaseFirst(String input){
        String output = null;
        try {
            output = input.substring(0,1).toUpperCase() + input.substring(1);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }

    }


    public static String convertMToKm(String input){
        try {
            double aDouble = Double.parseDouble(input);
            double km = aDouble / 1000;
            double kmRounded = Math.round(km * 10.0) / 10.0;
            String output = String.valueOf(kmRounded);
            if(output.endsWith("0")){
                return output.substring(0, output.length() - 2);
            }else {
                return output;
            }
        } catch (Exception e) {
            return "";
        }

    }


}
