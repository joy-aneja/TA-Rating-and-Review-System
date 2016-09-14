package com.example.joy.tarrgui.json;

import org.json.JSONObject;

/**
 * Created by Windows on 02-03-2015.
 */
public class Utils {
    public static boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }

}
