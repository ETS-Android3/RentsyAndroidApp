package com.menlopark.rentsyuser;

import android.content.Context;

import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by techiestown on 14/6/16.
 */
public class Config {
    public static final boolean DEBUG = true;
   // public static final String URL_BASE = "http://3.18.134.120";
   // public static final String URL_BASE = "http://34.198.166.165";
   public static final String URL_BASE = "http://3.19.212.234";
   // public static final String API_URL = URL_BASE + "/rentsy/apis/";
   public static final String API_URL = URL_BASE + "/rentsy/public/api/";
    public static String SELECTED_LOCATION = " ";

    public Config(Context context) {
    }

     public static void removePreference(String key)
     {
        Prefs.remove(key);

     }
    public static void setPreference(String valueKey, String value) {
        Prefs.putString(valueKey, value);
    }

    public static String getPreference(String valueKey) {
        return Prefs.getString(valueKey, "");
    }

}
