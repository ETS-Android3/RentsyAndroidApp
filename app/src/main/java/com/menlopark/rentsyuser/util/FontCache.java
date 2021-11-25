package com.menlopark.rentsyuser.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by techiestown on 15/6/16.
 */
public class FontCache {
    private static final String TAG = "FontCache";

    private static final String FONT_DIR = "fonts";

    private static Map<String, Typeface> cache = new HashMap<>();
    private static Map<String, String> fontMap = new HashMap<>();

    private Context mContext;
    private static FontCache sFontCache;

    public static FontCache getInstance(Context context){
        if (sFontCache == null){
            sFontCache = new FontCache(context);
        }
        return sFontCache;
    }

    public FontCache(Context context){
        mContext = context;
        AssetManager am = mContext.getResources().getAssets();
        try {
            String fileList[] = am.list(FONT_DIR);

            for (String fileName : fileList){
                String fontName = fileName.substring(0,fileName.lastIndexOf('.')).toLowerCase();
                fontMap.put(fontName,fileName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Typeface get(String fontName){
        String fontFileName = fontMap.get(fontName.toLowerCase());
        if (fontFileName == null){
            L.e(TAG,"font is not available:"+fontName);
            return null;
        }

        if (cache.containsKey(fontName)){
            return cache.get(fontName);
        }else {
            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), FONT_DIR+"/"+fontFileName);
            cache.put(fontFileName, typeface);
            return typeface;
        }
    }


}
