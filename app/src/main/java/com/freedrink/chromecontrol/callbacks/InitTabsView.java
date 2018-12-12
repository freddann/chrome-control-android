package com.freedrink.chromecontrol.callbacks;

import android.util.Log;

import com.freedrink.chromecontrol.components.tabview.TabsContent;
import com.freedrink.chromecontrol.http.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InitTabsView implements ResponseCallback {
    private final TabsContent tabs;

    public InitTabsView(TabsContent tabs){
        this.tabs = tabs;
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
                    tabs.addItemNoNotify(tab.getString("title"), tab.getString("url"));
                }
                tabs.update();
            } else {
                Log.e("InitTabsView", "No tabs in window" + window.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
