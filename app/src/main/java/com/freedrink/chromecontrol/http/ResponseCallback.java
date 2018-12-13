package com.freedrink.chromecontrol.http;

import android.util.Log;

import com.freedrink.chromecontrol.components.tabview.TabsContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseCallback {
    private final TabsContent tabs;

    public ResponseCallback(TabsContent tabs){
        this.tabs = tabs;
    }

    private void init(JSONArray allWindows){
        try {
            for (int i = 0; i < allWindows.length(); i++) {
            }

            JSONObject window = allWindows.getJSONObject(0);
            if (window.has("tabs")){
                tabs.clear();
                JSONArray allTabs = window.getJSONArray("tabs");
                for (int i = 0; i < allTabs.length(); i++){
                    JSONObject tab = allTabs.getJSONObject(i);
                    tabs.addItemNoNotify(tab.getString("title"), tab.getString("url"));
                    if (tab.getBoolean("focused")){
                        tabs.setSelected(i);
                    }
                }
                tabs.update();
            } else {
                Log.e("ResponseCallback", "No tabs in window" + window.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void focusTab(int index){
        tabs.setSelected(index);
    }

    private void createTab(JSONObject tab) {
        try {
            int pos = tabs.size();
            if (pos != tab.getInt("index"))
                Log.e("ResponseCallback", "Indexing error: " + pos + " != " + tab.getInt("index"));
            tabs.addItem(tab.getString("title"), tab.getString("url"));
            if (tab.getBoolean("focused"))
                tabs.setSelected(pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void closeTab(int index){
        tabs.removeItem(index);
    }

    private void updateTab(JSONObject tab) {
        try {
            int pos = tab.getInt("index");
            if (pos >= tabs.size())
                Log.e("ResponseCallback", "Indexing error: " + pos + " >= tabs.size()");
            tabs.replaceItem(pos, tab.getString("title"), tab.getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void call(String result){
        try {
            JSONObject content = new JSONObject(result);
            switch(content.getString("action")){
                case "init": init(content.getJSONArray("windows")); break;
                case "focusTab": focusTab(content.getInt("index")); break;
                case "createTab": createTab(content.getJSONObject("tab")); break;
                case "closeTab": closeTab(content.getInt("index")); break;
                case "updateTab": updateTab(content.getJSONObject("tab")); break;
                default: Log.d("ResponseCallback", content.toString());
            }
        } catch (JSONException|NullPointerException e) {
            e.printStackTrace();
        }
    }

}
