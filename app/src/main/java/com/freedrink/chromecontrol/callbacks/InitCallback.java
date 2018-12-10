package com.freedrink.chromecontrol.callbacks;

import android.util.Log;

import com.freedrink.chromecontrol.http.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;

public class InitCallback implements ResponseCallback {
    @Override
    public void call(String result) {
        try {
            JSONArray json = new JSONArray(result);
            for (int i = 0; i < json.length(); i++) {
                Log.d("My App", json.getJSONObject(i).toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
