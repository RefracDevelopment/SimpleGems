package me.refracdevelopment.simplegems.menu;

import me.refracdevelopment.simplegems.utilities.config.Menus;

import java.util.HashMap;
import java.util.Map;

public class GemShop {

    private final GemShopGUI shop;
    private final Map<String, GemShopItem> items;

    public GemShop() {
        this.shop = new GemShopGUI();
        this.items = new HashMap<>();
        Menus.GEM_SHOP_ITEMS.getKeys(false).forEach(item -> this.items.put(item, new GemShopItem(item)));
    }

    public GemShopGUI getGemShop() {
        return this.shop;
    }

    public Map<String, GemShopItem> getItems() {
        return this.items;
    }
}