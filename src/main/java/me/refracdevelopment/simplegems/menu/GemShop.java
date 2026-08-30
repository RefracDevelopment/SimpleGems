package me.refracdevelopment.simplegems.menu;

import lombok.Getter;
import me.refracdevelopment.simplegems.SimpleGems;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class GemShop {

    private final Map<String, List<GemShopItem>> categories;

    public GemShop() {
        this.categories = new HashMap<>();

        setupCustomMenuData();
    }

    public void setupCustomMenuData() {
        categories.clear();

        List<GemShopItem> items = new ArrayList<>();

        ConfigurationSection section = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES;
        ConfigurationSection confirmationSection = SimpleGems.getInstance().getMenus().CONFIRMATION_MENU;

        section.getKeys(false).forEach(gemShopCategory -> {
            ConfigurationSection category = section.getConfigurationSection(gemShopCategory + ".items");

            if (category != null) {
                category.getKeys(false).forEach(item -> items.add(new GemShopItem(gemShopCategory, item, false)));

                categories.put(gemShopCategory, items);
            }
        });

        confirmationSection.getKeys(false).forEach(confirmation -> {
            ConfigurationSection configItems = confirmationSection.getConfigurationSection("items");

            if (configItems != null) {
                configItems.getKeys(false).forEach(item -> items.add(new GemShopItem("confirmation-menu", item, true)));

                categories.put("confirmation-menu", items);
            }
        });
    }

    public List<GemShopItem> getItems(String categoryName) {
        return categories.get(categoryName);
    }
}