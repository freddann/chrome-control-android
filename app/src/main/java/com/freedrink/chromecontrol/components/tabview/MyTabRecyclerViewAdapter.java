package com.freedrink.chromecontrol.components.tabview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freedrink.chromecontrol.R;
import com.freedrink.chromecontrol.components.tabview.TabFragment.OnListFragmentInteractionListener;
import com.freedrink.chromecontrol.components.tabview.TabsContent.TabItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TabItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTabRecyclerViewAdapter extends RecyclerView.Adapter<MyTabRecyclerViewAdapter.ViewHolder> implements TabsContent.ListListener {

    private final TabsContent mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyTabRecyclerViewAdapter(TabsContent items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TabItem item = mValues.get(position);
            holder.mTitleView.setText(item.title);
            holder.mUrlView.setText(item.url);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(item);
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onRemove(TabItem item, int position) {
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public void updateContent(){
        notifyDataSetChanged();
    }

    @Override
    public void onAdd(TabItem item, int position) {
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mValues.size());
    }

    @Override
    public void onUpdate(TabItem item, int position) {
        notifyItemInserted(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView, mUrlView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mUrlView = (TextView) view.findViewById(R.id.url);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

}
