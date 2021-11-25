package com.menlopark.rentsyuser.util.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mushtakim Ansari on 11/3/2015.
 * Utility for secure preference
 */
public class PreferenceUtil {
    private static final String PREFERENCE_FILE = "RENTSY";
    private static final String PASSWORD = "RENTSY";
    private static SharedPreferences preferences;

    public synchronized static SharedPreferences getPref(Context context) {
        if (preferences == null)
            preferences = new ObscuredSharedPreferences(context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE), PASSWORD);
        return preferences;
    }


}