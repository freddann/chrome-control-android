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
    private final List<TabItem> ITEMS = new ArrayList<TabItem>();

    private int selectedIndex = 0;
    private TabsContent.Listener listener = null;

    public void setListener(TabsContent.Listener listener){
        this.listener = listener;
    }

    public TabItem addItemNoNotify(String title, String url){
        TabItem item = new TabItem(title, url);
        ITEMS.add(item);
        return item;
    }

    public void addItem(String title, String url){
        int index = size();
        TabItem item = addItemNoNotify(title, url);
        if (listener != null)
            listener.onAdd(item, index);
    }

    public void removeItem(int index){
        if (index <= selectedIndex){
            selectPrevious();
        }
            TabItem item = ITEMS.remove(index);
            if (listener != null){
                listener.onRemove(item, index);
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

    public int indexOf(TabItem item){
        return ITEMS.indexOf(item);
    }

    public void setSelected(int index){
        selectedIndex = index;
    }

    public void selectPrevious(){
        selectedIndex = Math.max(0, selectedIndex-1);
    }

    public void selectNext(){
        selectedIndex = Math.min(size(), selectedIndex+1);
    }

    public int getSelectedIndex(){
        return selectedIndex;
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
        public final String title;
        public final String url;

        public TabItem(String title, String url) {
            this.title = title;
            this.url = url;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    public interface Listener{
        public void onRemove(TabItem item, int position);
        public void onAdd(TabItem item, int position);
        public void updateContent();
    }
}
