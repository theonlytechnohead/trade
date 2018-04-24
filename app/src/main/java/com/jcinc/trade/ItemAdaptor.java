package com.jcinc.trade;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemAdaptor extends RecyclerView.Adapter<ItemAdaptor.ItemViewHolder> {
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView itemName;
        TextView itemID;
        ImageView itemImage;

        ItemViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cardView);
            itemName = itemView.findViewById(R.id.item_name);
            itemID = itemView.findViewById(R.id.item_id);
            itemImage = itemView.findViewById(R.id.item_image);
        }
    }

    private List<Item> items;
    ItemAdaptor(List<Item> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.itemName.setText(items.get(i).name);
        itemViewHolder.itemID.setText(items.get(i).id);
        itemViewHolder.itemImage.setImageResource(items.get(i).image);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
