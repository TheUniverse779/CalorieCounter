package com.caloriecounter.calorie;

public class Constant {
    public static final String API_KEY = "c22b4dea384401eacaa52782f30f88d3";
//    public static final String API_KEY_ACCU = "ekhA5PxPCm3KgNImGcXtjUJqRd4Rt3Cb"; // app 10tr
    public static final String API_KEY_WEATHER_API = "c9757897b2f544e7b2e23652232003";

    public static class RemoteConfigKey{
        public static final String SHOW_REWARD = "show_reward";
        public static final String NEW_REWARD = "new_reward";
    }


    public static class SharePrefKey {
        public static final String LAT_LOCATION = "lat_location";
        public static final String LNG_LOCATION = "log_location";
        public static final String CITY_NAME = "CITY_NAME";
        public static final String USING_MY_LOCATION = "using my location";
        public static final String SHOW_REWARD = "SHOW_REWARD";
        public static final String NEW_REWARD = "new_reward";
        public static final String COUNT_REWARD = "countReward";
        public static final String COIN = "COIN";

        public static final String DAY = "DAY";
        public static final String IMAGE = "IMAGE";
    }



    public static class Setting {
        public static final String NOTIFICATION = "NOTIFICATION";
        public static final String TEMP_UNIT = "TEMP_UNIT";
        public static final String TIME_FORMAT = "TIME_FORMAT";
        public static final String DATE_FORMAT = "DATE_FORMAT";
        public static final String WIND_SPEED = "WIND_SPEED";
        public static final String RAIN_PROBABILITY = "RAIN_PROBABILITY";
        public static final String PRESSURE = "PRESSURE";
        public static final String VISIBILITY = "VISIBILITY";
    }


    public static class SettingValue {
        public static final String NOTIFICATION = "NOTIFICATION";
        public static final String TEMP_UNIT_C = "TEMP_UNIT_C";
        public static final String TEMP_UNIT_F = "TEMP_UNIT_F";
        public static final String TIME_FORMAT_24 = "HH:mm";
        public static final String TIME_FORMAT_12 = "hh:mm";
        public static final String DATE_FORMAT = "DATE_FORMAT";
        public static final String WIND_SPEED_KMH = "WIND_SPEED_kmh";
        public static final String WIND_SPEED_MS = "WIND_SPEED_ms";
        public static final String RAIN_PROBABILITY_MM = "RAIN_PROBABILITY_MM";
        public static final String RAIN_PROBABILITY_CM = "RAIN_PROBABILITY_CM";
        public static final String RAIN_PROBABILITY_IN = "RAIN_PROBABILITY_IN";
        public static final String PRESSURE_mbar = "mbar";
        public static final String PRESSURE_bar = "bar";
        public static final String PRESSURE_atm = "atm";
        public static final String PRESSURE_kPa = "kPa";
        public static final String PRESSURE_mmHg = "mmHg";
        public static final String PRESSURE_inHg = "inHg";
        public static final String PRESSURE_hPa = "hPa";
        public static final String PRESSURE_psi = "psi";
        public static final String VISIBILITY_KM = "VISIBILITY_KM";
        public static final String VISIBILITY_M = "VISIBILITY_M";
    }

    public static final String BACKgROUND = "BACKgROUND";
    public static final String BACKgROUND_DEFAULT = "bg_01d";



    public static class ContentType{
        public static final String FREE = "free";
        public static final String PRIVATE = "private";
    }

    public static class SortBy{
        public static final String RATING = "rating";
        public static final String DOWNLOAD = "downloads";
        public static final String FAVORITES = "favorites";
        public static final String NEWEST = "";
    }

    public static class SortByDouble{
        public static final String RATING = "rating";
        public static final String NEWEST = "date";
    }

    public static class ScreenSize{
        public static final String WIDTH = "1080";
        public static final String HEIGHT = "2340";
    }


    public static class IntentKey{
        public static final String CATE_ID = "CATE_ID";
        public static final String TYPE_TO_GET_IMAGE = "TYPE_TO_GET_IMAGE";
        public static final String SORT_BY = "SORT_BY";
        public static final String PRESENT_IMAGE_TYPE = "PRESENT_IMAGE_TYPE";
        public static final String KEYWORD = "KEYWORD";
        public static final String CATEGORY = "CATEGORY";
        public static final String LIST_IMAGE = "LIST_IMAGE";
        public static final String POSITION = "POSITION";
        public static final String TYPE_MY_WALLPAPER = "TYPE_MY_WALLPAPER";
        public static final String LOAD_MORE_ABLE = "LOAD_MORE_ABLE";
        public static final String DATA = "DATA";
        public static final String IS_FROM_NOTIFICATION = "IS_FROM_NOTIFICATION";
        public static final String IDS = "IDS";
        public static final String IMAGE_ID = "IMAGE_ID";
        public static final String URL = "url";
        public static final String NAME = "name";


    }

    public static class PrefKey{
        public static final String RECENT_SEARCH = "RECENT_SEARCH";
        public static final String LIST_IMAGE = "LIST_IMAGE";
        public static final String LIST_SIMILAR_IMAGE = "LIST_SIMILAR_IMAGE";
        public static final String COUNT_CLICK_MENU = "COUNT_CLICK_MENU";
        public static final String IS_SHOW_DIALOG = "IS_SHOW_DIALOG";
        public static final String SCHEDULE = "SCHEDULE";
        public static final String IS_SHOW_DIALOG_AUTO_CHANGE = "IS_SHOW_DIALOG_AUTO_CHANGE";



    }


    public static class MyWallpaperType{
        public static final String RECENT = "RECENT";
        public static final String FAVORITE = "FAVORITE";
    }


    public static class PresentImageType{
        public static final String HOME = "HOME";
        public static final String SPECIAL = "SPECIAL";
        public static final String CATEGORY = "CATEGORY";
        public static final String MY_IMAGE = "MY_IMAGE";
        public static final String SEARCH = "SEARCH";
    }


    public static class Event{
        public static final String SET_HOME_WALLPAPER_SUCCESS = "SET_HOME_WALLPAPER_SUCCESS";
        public static final String SET_LOCK_WALLPAPER_SUCCESS = "SET_LOCK_WALLPAPER_SUCCESS";
        public static final String DOWNLOAD_SUCCESS = "DOWNLOAD_SUCCESS";
        public static final String SHARE_SUCCESS = "SHARE_SUCCESS";
        public static final String SHARE_EXCEPTION = "SHARE_EXCEPTION";
        public static final String VIEW_PHOTO = "VIEW_PHOTO";
        public static final String VIEW_DOUBLE_PHOTO = "VIEW_DOUBLE_PHOTO";
        public static final String CHANGE_MAIN_TAB = "CHANGE_MAIN_TAB";
        public static final String lOAD_MORE_IN_MAIN_TAB = "lOAD_MORE_IN_MAIN_TAB";
        public static final String lOAD_MORE_SIMILAR = "lOAD_MORE_SIMILAR";
        public static final String lOAD_MORE_NEWEST = "lOAD_MORE_NEWEST";
        public static final String PRESS_MENU = "PRESS_MENU";
        public static final String RATING = "RATING";
        public static final String GET_BITMAP_FAIL = "GET_BITMAP_FAIL";
        public static final String SEARCH = "SEARCH";



        public static final String CHECK_SCREEN = "CHECK_SCREEN";


    }

    public static class Action{
        public static final String DOWNLOAD = "download";
        public static final String FAVORITE = "favorite";
        public static final String SET = "set";


    }

    public static class RemoteConfig{
        public static final String IS_SHOW_ADS = "IS_SHOW_ADS";
        public static final String VERSION = "VERSION";
        public static final String NEED_REQUIRE_UPDATE = "NEED_REQUIRE_UPDATE";
        public static final String UPDATE_MESSAGE = "UPDATE_MESSAGE";

    }


}
