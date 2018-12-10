package com.freedrink.chromecontrol.callbacks;

import android.util.Log;

import com.freedrink.chromecontrol.components.MyTabRecyclerViewAdapter;
import com.freedrink.chromecontrol.components.dummy.TabsContent;
import com.freedrink.chromecontrol.http.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InitCallback implements ResponseCallback {
    private final TabsContent tabs;
    private final MyTabRecyclerViewAdapter adapter;

    public InitCallback(TabsContent tabs, MyTabRecyclerViewAdapter adapter){
        this.tabs = tabs;
        this.adapter = adapter;
    }
    @Override
    public void call(String result) {
        try {
            JSONArray allWindows = new JSONArray(result);
            for (int i = 0; i < allWindows.length(); i++) {
            }

            JSONObject window = allWindows.getJSONObject(0);
            if (window.has("tabs")){
                tabs.clear();
                JSONArray allTabs = window.getJSONArray("tabs");
                for (int i = 0; i < allTabs.length(); i++){
                    JSONObject tab = allTabs.getJSONObject(i);
                    Log.d("My App", tab.toString());
                    tabs.addItem(tab.getInt("index"), tab.getString("title"), tab.getString("url"));
                }
                adapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
