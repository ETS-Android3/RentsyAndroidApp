package com.menlopark.rentsyuser.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by techiestown on 14/6/16.
 */
public class UiUtil {
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getCountAsTwoDigit(int count){
        if (count < 0) return "";
        if (count < 10) return "0"+count;
        return count+"";
    }
}
