package com.jcinc.trade;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemAdaptor extends RecyclerView.Adapter<ItemAdaptor.ItemViewHolder> {
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        this.listener = listener;
    }

    public String itemID (int position) {
        return items.get(position).id;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView itemName;
        TextView itemCondition;
        TextView itemID;
        ImageView itemImage;

        ItemViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            cv = itemView.findViewById(R.id.cardView);
            itemName = itemView.findViewById(R.id.item_name);
            itemCondition = itemView.findViewById(R.id.item_condition);
            itemID = itemView.findViewById(R.id.item_id);
            itemImage = itemView.findViewById(R.id.item_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
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
        return new ItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.itemName.setText(items.get(i).name);
        itemViewHolder.itemCondition.setText(convertCondtion(items.get(i).condition));
        itemViewHolder.itemID.setText(items.get(i).id);
        itemViewHolder.itemImage.setImageResource(items.get(i).image);
    }

    private String convertCondtion (int condition) {
        if (100 >= condition && condition > 75) {
            return "Good condition";
        } else if (condition > 50) {
            return "Used";
        } else if (condition > 25) {
            return "Damaged";
        } else if (condition > 1) {
            return "Breaking";
        }
        return "";
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
