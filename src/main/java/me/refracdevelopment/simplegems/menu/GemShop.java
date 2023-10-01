package me.refracdevelopment.simplegems.menu;

import lombok.Getter;
import me.refracdevelopment.simplegems.manager.configuration.cache.Menus;

import java.util.HashSet;
import java.util.Set;

@Getter
public class GemShop {

    private final Set<GemShopItem> items;

    public GemShop() {
        this.items = new HashSet<>();
        Menus.GEM_SHOP_CATEGORIES.getKeys(false).forEach(category -> {
            Menus.GEM_SHOP_CATEGORIES.getConfigurationSection(category + ".items").getKeys(false).forEach(item -> {
                this.items.add(new GemShopItem(new GemShopCategory(null, category), item));
            });
        });
    }

    public void reload() {
        this.items.clear();
        Menus.GEM_SHOP_CATEGORIES.getKeys(false).forEach(category -> {
            GemShopCategory gemShopCategory = new GemShopCategory(null, category);
            Menus.GEM_SHOP_CATEGORIES.getConfigurationSection(category + ".items").getKeys(false).forEach(item -> {
                this.items.add(new GemShopItem(gemShopCategory, item));
            });
        });
    }
}