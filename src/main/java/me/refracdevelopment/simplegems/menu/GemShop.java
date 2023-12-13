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

    private final Map<String, List<GemShopItem>> categories;

    public GemShop() {
        this.categories = new HashMap<>();
        load();
    }

    public void load() {
        this.categories.clear();

        List<GemShopItem> items = new ArrayList<>();

        Section section = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES;

        section.getRoutesAsStrings(false).forEach(gemShopCategory -> {
            Section category = section.getSection(gemShopCategory + ".items");

            category.getRoutesAsStrings(false).forEach(item -> {
                items.add(new GemShopItem(gemShopCategory, item));
            });

            this.categories.put(gemShopCategory, items);
        });
    }

    public List<GemShopItem> getItems(String categoryName) {
        return categories.get(categoryName);
    }
}