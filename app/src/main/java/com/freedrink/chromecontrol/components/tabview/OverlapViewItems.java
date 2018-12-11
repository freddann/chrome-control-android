package com.freedrink.chromecontrol.components.tabview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class OverlapViewItems extends RecyclerView.ItemDecoration {

    //Following code from : http://stackoverflow.com/questions/27633454/how-to-overlap-items-in-linearlayoutmanager-recyclerview-like-stacking-cards
    private final static int overlap = 250;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);


        final int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == 0) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, -overlap, 0, 0);
        }
    }
}
