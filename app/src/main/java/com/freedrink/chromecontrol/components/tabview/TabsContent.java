package com.freedrink.chromecontrol.components.tabview;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample title for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TabsContent {
    private final List<TabItem> ITEMS = new ArrayList<TabItem>();

    private int selectedIndex = 0;
    private ListListener listener = null;
    private SelectedItemListener selectedItemListener = null;

    public void setListener(ListListener listener){
        this.listener = listener;
    }
    public void setSelectedItemListener(SelectedItemListener listener) { this.selectedItemListener = listener; }

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
        onUpdateSelectedIndex();
    }

    public void selectPrevious(){
        selectedIndex--;
        if (selectedIndex < 0){
            selectedIndex = size() - 1;
        }
        onUpdateSelectedIndex();
    }

    public void selectNext(){
        selectedIndex++;
        if (selectedIndex >= size()){
            selectedIndex = 0;
        }
        onUpdateSelectedIndex();
    }

    public void replaceItem(int index, String title, String url){
        TabItem item = new TabItem(title, url);
        ITEMS.set(index, item);
        if (listener != null)
            listener.onUpdate(item, index);
        if (index == selectedIndex)
            onUpdateSelectedIndex();
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
        onUpdateSelectedIndex();

    }

    private void onUpdateSelectedIndex(){
        if (selectedItemListener != null)
            selectedItemListener.onUpdate(ITEMS.get(selectedIndex), selectedIndex);
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

    public interface ListListener {
        public void onRemove(TabItem item, int position);
        public void onAdd(TabItem item, int position);
        public void onUpdate(TabItem item, int position);
        public void updateContent();
    }

    public interface SelectedItemListener {
        public void onUpdate(TabItem item, int position);
    }
}
