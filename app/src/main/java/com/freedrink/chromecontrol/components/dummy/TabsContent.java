package com.freedrink.chromecontrol.components.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample title for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TabsContent {
    public final List<TabItem> ITEMS = new ArrayList<TabItem>();
    public final Map<String, TabItem> ITEM_MAP = new HashMap<String, TabItem>();

    public void addItem(int index, String title, String url){
        TabItem item = new TabItem(String.valueOf(index), title, url);
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public TabItem get(int index){
        return ITEM_MAP.get(String.valueOf(index));
    }

    public int size(){
        return ITEMS.size();
    }

    public void clear(){
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    /**
     * A dummy item representing a piece of title.
     */
    public static class TabItem {
        public final String id;
        public final String title;
        public final String url;

        public TabItem(String id, String title, String url) {
            this.id = id;
            this.title = title;
            this.url = url;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
