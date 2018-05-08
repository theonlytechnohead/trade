package com.jcinc.trade;

import java.util.ArrayList;

class Item {
    String name;
    int condition;
    String id;
    int image;
    String item_user_id;
    String[] action_names;
    String[] actions;

    Item(
        String name,
        int condition,
        String id, int image,
        String item_user_id,
        String action_names,
        String actions
    ) {
        this.name = name;
        this.condition = condition;
        this.id = id;
        this.image = image;
        this.item_user_id = item_user_id;
        this.action_names = action_names.split(", ");
        this.actions = actions.split(", ");
    }
}
