package com.jcinc.trade;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemAdaptor extends RecyclerView.Adapter<ItemAdaptor.ViewHolder> {
    private String[] dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public ViewHolder(TextView view) {
            super(view);
            textView = view;
        }
    }

    public ItemAdaptor(String[] data) {
        dataSet = data;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ItemAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(dataSet[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}
