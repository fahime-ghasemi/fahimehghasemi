package com.fahimehghasemi.freesamplecode.recyclerview.endless;

import android.content.Context;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by fahime on 8/3/18.
 */

public class Util {

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("news.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
