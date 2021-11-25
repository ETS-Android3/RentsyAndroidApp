package com.menlopark.rentsyuser.util;

import android.util.Log;

import com.menlopark.rentsyuser.Config;



public class L {
    public static void d(String tag, String msg){
        if (Config.DEBUG) Log.d(tag,msg==null?"null":msg);
    }

    public static void e(String tag, String msg){
        if (Config.DEBUG) Log.e(tag,msg==null?"null":msg);
    }

    public static void i(String tag, String msg){
        if (Config.DEBUG) Log.i(tag,msg==null?"null":msg);
    }
}
