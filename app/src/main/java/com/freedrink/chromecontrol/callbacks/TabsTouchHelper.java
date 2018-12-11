package com.freedrink.chromecontrol.callbacks;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.freedrink.chromecontrol.components.tabview.MyTabRecyclerViewAdapter;
import com.freedrink.chromecontrol.components.tabview.TabsContent;

public class TabsTouchHelper extends ItemTouchHelper {

    public TabsTouchHelper(final TabsContent content){
        super(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Log.d("Swipe", viewHolder.toString() + String.valueOf(i));
                int pos = viewHolder.getAdapterPosition();
                content.removeItem(pos);
            }
        });
    }
}
