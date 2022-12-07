package me.refracdevelopment.simplegems.menu;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import me.refracdevelopment.simplegems.utilities.files.Menus;

import java.util.HashMap;
import java.util.Map;

public class GemShop extends Manager {

    private final GemShopGUI shop;
    private final Map<String, GemShopItem> items;

    public GemShop(RosePlugin rosePlugin) {
        super(rosePlugin);
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

    @Override
    public void reload() {
        this.items.clear();
        Menus.GEM_SHOP_ITEMS.getKeys(false).forEach(item -> this.items.put(item, new GemShopItem(item)));
    }

    @Override
    public void disable() {
        this.items.clear();
    }
}