package com.menlopark.rentsyuser.util;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@SuppressWarnings("deprecation")
public class AppUtility {

    public final static String TAG = "RentsyUser";

    public static final int INT_STATUS_SUCCESS = 1;

    public static final int INT_STATUS_FAILED_DOWNLOAD = -10;
    public static final int INT_STATUS_FAILED_CLIENT = -11;
    public static final int INT_STATUS_FAILED_TIMEOUT = -13;
    public static final int INT_STATUS_FAILED_IO = -12;
    public static final String filePath = Environment.getExternalStorageDirectory() + File.separator + ".Gumtrue" + File.separator;
    public static final long A_SECOND = 1000;
    public static final long A_MINUTE = 60 * A_SECOND;


    @SuppressLint("SimpleDateFormat")
    public static final String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String display = format.format(cal.getTimeInMillis());
        format = null;
        return display;
    }

    @SuppressLint("SimpleDateFormat")
    public static final String convertDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            String newFormat = formatter.format(testDate);
            System.out.println("...Date..." + newFormat);
            return newFormat;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }

    public static final Calendar utcToLocal(String dt){
        Calendar c = null;
        if (dt!=null){
            try {
                SimpleDateFormat formatter, FORMATTER;
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String oldDate = dt;
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = formatter.parse(oldDate);

                c = Calendar.getInstance();
                c.setTime(date);

                c.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return c;
    }

    public static final Calendar localToGMT(String dt){
        Calendar c = null;

        try {
            SimpleDateFormat formatter, FORMATTER;
            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String oldDate = dt;
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = formatter.parse(oldDate);
            c = Calendar.getInstance();
            c.setTime(date);
            c.setTimeZone(TimeZone.getTimeZone("GMT"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c;
    }

    public interface DelayCallback{
        void afterDelay();
    }

    public static void delay(int secs, final DelayCallback delayCallback){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, secs * 1000); // afterDelay will be executed after (secs*1000) milliseconds.
    }

    public static final String formatOrderDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            String newFormat = formatter.format(testDate);
            System.out.println(".....Date..." + newFormat);
            return newFormat;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }

    public static boolean isServiceRunning(String serviceName, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if(serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @SuppressLint("SimpleDateFormat")
    public static final String getCurrentDateOnly() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String display = format.format(cal.getTimeInMillis());
        format = null;
        return display;
    }

    @SuppressLint("SimpleDateFormat")
    public static final String getCurrentDate(Calendar cal) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String display = format.format(cal.getTime());
        format = null;
        return display;
    }

    @SuppressLint("SimpleDateFormat")
    public static final String getCurrentDate(final long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String display = format.format(time);
        format = null;
        return display;
    }

    @SuppressLint("SimpleDateFormat")
    public static final String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String display = format.format(cal.getTimeInMillis());
        format = null;
        return display;
    }

    public static final String generateToken() {
        SecureRandom random = new SecureRandom();
        BigInteger intValue = new BigInteger(35, random);
        String token = intValue.toString(32);
        intValue = null;
        random = null;
        return token;
    }


    public static final boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    static Map<Integer, String> mapKeycodes;

    public static final void loadKeyCodes() {

        if (mapKeycodes == null) {
            mapKeycodes = new HashMap<Integer, String>();
            mapKeycodes.put(135, "Scan");
            mapKeycodes.put(4, "Esc");

            mapKeycodes.put(67, "Backspace");
            mapKeycodes.put(66, "Enter");

            mapKeycodes.put(19, "Up Arrow");
            mapKeycodes.put(20, "Down Arrow");
            mapKeycodes.put(21, "Left Arrow");
            mapKeycodes.put(22, "Right Arrow");
            mapKeycodes.put(5, "Green Call Button");

            mapKeycodes.put(131, "F1");
            mapKeycodes.put(132, "F2");
            mapKeycodes.put(133, "Fn");
            mapKeycodes.put(134, "Aa");

            mapKeycodes.put(8, "1");
            mapKeycodes.put(9, "2");
            mapKeycodes.put(10, "3");
            mapKeycodes.put(11, "4");
            mapKeycodes.put(12, "5");
            mapKeycodes.put(13, "6");
            mapKeycodes.put(14, "7");
            mapKeycodes.put(15, "8");
            mapKeycodes.put(16, "9");
            mapKeycodes.put(7, "0");

            mapKeycodes.put(56, ".");
            mapKeycodes.put(62, "Bottom Right");
        }
    }

    public static final String getKeyString(int keyCode) {
        loadKeyCodes();
        return mapKeycodes.get(keyCode);
    }

    public static void appendLog(String text)
    {
        File logFile = new File("/storage/emulated/0/log.txt");
        if (!logFile.exists())
        {
            try
            { logFile.createNewFile(); }
            catch (IOException e)
            { e.printStackTrace(); }
        }
        try
        {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
