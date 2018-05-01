package com.jcinc.trade;

class Item {
    String name;
    int condition;
    String id;
    int image;
    String item_user_id;

    Item(String name, int condition, String id, int image, String item_user_id) {
        this.name = name;
        this.condition = condition;
        this.id = id;
        this.image = image;
        this.item_user_id = item_user_id;
    }
}
