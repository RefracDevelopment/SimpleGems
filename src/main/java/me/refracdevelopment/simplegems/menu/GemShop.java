package me.refracdevelopment.simplegems.menu;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import lombok.Getter;
import me.refracdevelopment.simplegems.SimpleGems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class GemShop {

    private final Map<GemShopCategory, List<GemShopItem>> categories;
    private final List<GemShopItem> items;

    public GemShop() {
        this.categories = new HashMap<>();
        this.items = new ArrayList<>();
        load();
    }

    public void load() {
        this.categories.clear();
        this.items.clear();

        Section section = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES;

        section.getKeys().forEach(category -> {
            GemShopCategory gemShopCategory = new GemShopCategory(null, category.toString());

            section.getSection(category + ".items").getKeys().forEach(item -> {
                this.items.add(new GemShopItem(gemShopCategory, item.toString()));
            });

            this.categories.put(gemShopCategory, this.items);
        });
    }
}