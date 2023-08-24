package com.caloriecounter.calorie.ui.charging;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.ui.charging.model.ChargingAnimation;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class AnimationLoader {

    public static void load(Task<ArrayList<ChargingAnimation>> task){
        String config = "http://d1khtkjgtg9c4c.cloudfront.net/wallpaper/ChargingAnimation/file.json";
        RequestQueue queue = Volley.newRequestQueue(WeatherApplication.get());
        queue.getCache().clear();
        queue.add(new StringRequest(Request.Method.GET, config, response -> {
            ArrayList<ChargingAnimation> chargingAnimations = new ArrayList<>();
            if(response==null||TextUtils.isEmpty(response)){
                task.success(null);
            }else {
                try {
                    JSONObject object =  new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String file = jsonObject.getString("file");
                        String textColor = jsonObject.getString("text_color");
                        String type = jsonObject.getString("type");
                        String tag = jsonObject.getString("tag");
                        String alpha = jsonObject.getString("alpha");
                        String x = jsonObject.getString("x");
                        String y = jsonObject.getString("y");
                        String rewardAds = jsonObject.getString("reward_ads");
                        ChargingAnimation chargingAnimation = new ChargingAnimation(file,textColor,Integer.parseInt(type)
                                ,tag,Float.parseFloat(alpha),Double.parseDouble(x),
                                Double.parseDouble(y),rewardAds.equals("1"));
                        chargingAnimations.add(chargingAnimation);

                    }
                    task.success(chargingAnimations);
                    DebugLog.e("AnimationLoader",chargingAnimations.size()+"");
                }catch (Exception e){
                    e.printStackTrace();
                    task.success(null);
                    DebugLog.e("AnimationLoader","Error "+e.getMessage());
                }
            }
        }, error -> task.success(null)));

    }

    public static String getPreviewLink(String fileName){
        return "https://apps.ezlifetech.com/Battery_Animation/"+fileName+".webp";
    }

    public static String getVideoLink(String fileName){
        return "http://d1khtkjgtg9c4c.cloudfront.net/wallpaper/ChargingAnimation/"+fileName+".mp4";
    }
    public static String getVideoPath(String fileName){
        return new File(WeatherApplication.get().getFilesDir(),fileName+".mp4").getAbsolutePath();
    }
    public static boolean isDownload(String fileName){
        File file = new File(getVideoPath(fileName));
        return file.exists()&&file.canRead();
    }

}
