package com.menlopark.rentsyuser.util;

import android.content.Context;
import android.widget.Toast;

public class ToastMsg {

    public static void showLong(Context context, String message){
        Toast.makeText(context,message, Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String message){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }
}
