package com.jcinc.trade;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

public class ActionAdaptor extends RecyclerView.Adapter<ActionAdaptor.ActionViewHolder> {

    static class ActionViewHolder extends RecyclerView.ViewHolder {
        Button actionButton;

        ActionViewHolder (View actionView) {
            super(actionView);
            actionButton = actionView.findViewById(R.id.action_button);
        }
    }

    private List<Action> actions;
    ActionAdaptor(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public int getItemCount () {
        return actions.size();
    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_button_layout, parent, false);
        return new ActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder actionViewHolder, int i) {
        actionViewHolder.actionButton.setText(actions.get(i).name);
    }

    @Override
    public void onAttachedToRecyclerView (@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
