package com.freedrink.chromecontrol.components.tabview;

import android.util.Log;

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

    private TabsContent.Listener listener = null;

    public void setListener(TabsContent.Listener listener){
        this.listener = listener;
    }

    public TabItem addItemNoNotify(int index, String title, String url){
        TabItem item = new TabItem(index, title, url);
        ITEMS.add(item);
        return item;
    }

    public void addItem(int index, String title, String url){
        TabItem item = addItemNoNotify(index, title, url);
        if (listener != null)
            listener.onAdd(item);
    }

    public void removeItem(int index){
            TabItem item = ITEMS.remove(index);
            if (listener != null){
                listener.onRemove(item);
        }
    }

    public TabItem get(int index){
        if (index < ITEMS.size())
            return ITEMS.get(index);
        else {
            Log.e("TabsContent", "Index out of range: " + index + size());
            return null;
        }
    }

    public int size(){
        return ITEMS.size();
    }

    public void clear(){
        ITEMS.clear();
    }

    public void update(){
        if (listener != null)
            listener.updateContent();
    }

    /**
     * A dummy item representing a piece of title.
     */
    public static class TabItem {
        public final int id;
        public final String title;
        public final String url;

        public TabItem(int id, String title, String url) {
            this.id = id;
            this.title = title;
            this.url = url;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    public interface Listener{
        public void onRemove(TabItem item);
        public void onAdd(TabItem item);
        public void updateContent();
    }
}
