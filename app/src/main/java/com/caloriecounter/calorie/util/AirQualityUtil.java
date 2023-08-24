package com.caloriecounter.calorie.util;

import com.caloriecounter.calorie.Constant;
import com.caloriecounter.calorie.R;

public class AirQualityUtil {
    public static int getAQISO2(String input) {
        double aDouble = Double.parseDouble(input);
        if (aDouble >= 0 && aDouble < 20) {
            return R.color.color_good;
        } else if (aDouble >= 20 && aDouble < 80) {
            return R.color.color_fair;
        } else if (aDouble >= 80 && aDouble < 250) {
            return R.color.color_moderate;
        } else if (aDouble >= 250 && aDouble < 350) {
            return R.color.color_poor;
        } else {
            return R.color.color_very_poor;
        }
    }


    public static int getAQINO2(String input) {
        double aDouble = Double.parseDouble(input);
        if (aDouble >= 0 && aDouble < 40) {
            return R.color.color_good;
        } else if (aDouble >= 40 && aDouble < 70) {
            return R.color.color_fair;
        } else if (aDouble >= 70 && aDouble < 150) {
            return R.color.color_moderate;
        } else if (aDouble >= 150 && aDouble < 200) {
            return R.color.color_poor;
        } else {
            return R.color.color_very_poor;
        }
    }

    public static int getAQIPM10(String input) {
        double aDouble = Double.parseDouble(input);
        if (aDouble >= 0 && aDouble < 20) {
            return R.color.color_good;
        } else if (aDouble >= 20 && aDouble < 50) {
            return R.color.color_fair;
        } else if (aDouble >= 50 && aDouble < 100) {
            return R.color.color_moderate;
        } else if (aDouble >= 100 && aDouble < 200) {
            return R.color.color_poor;
        } else {
            return R.color.color_very_poor;
        }
    }


    public static int getAQIPM25(String input) {
        double aDouble = Double.parseDouble(input);
        if (aDouble >= 0 && aDouble < 10) {
            return R.color.color_good;
        } else if (aDouble >= 10 && aDouble < 25) {
            return R.color.color_fair;
        } else if (aDouble >= 25 && aDouble < 50) {
            return R.color.color_moderate;
        } else if (aDouble >= 50 && aDouble < 75) {
            return R.color.color_poor;
        } else {
            return R.color.color_very_poor;
        }
    }

    public static int getAQIO3(String input) {
        double aDouble = Double.parseDouble(input);
        if (aDouble >= 0 && aDouble < 60) {
            return R.color.color_good;
        } else if (aDouble >= 60 && aDouble < 100) {
            return R.color.color_fair;
        } else if (aDouble >= 100 && aDouble < 140) {
            return R.color.color_moderate;
        } else if (aDouble >= 140 && aDouble < 180) {
            return R.color.color_poor;
        } else {
            return R.color.color_very_poor;
        }
    }

    public static int getAQICO(String input) {
        double aDouble = Double.parseDouble(input);
        if (aDouble >= 0 && aDouble < 4400) {
            return R.color.color_good;
        } else if (aDouble >= 4400 && aDouble < 9400) {
            return R.color.color_fair;
        } else if (aDouble >= 9400 && aDouble < 12400) {
            return R.color.color_moderate;
        } else if (aDouble >= 12400 && aDouble < 15400) {
            return R.color.color_poor;
        } else {
            return R.color.color_very_poor;
        }
    }




    public static int getPercentSeekBar(int input) {
        if (input > 300) {
            return 100;
        } else {
            return (int) (( (float) input /  350f) * 100);
        }
    }


}
