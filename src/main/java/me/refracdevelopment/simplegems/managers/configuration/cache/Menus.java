package me.refracdevelopment.simplegems.managers.configuration.cache;

import me.refracdevelopment.simplegems.SimpleGems;
import org.bukkit.configuration.ConfigurationSection;

public class Menus {

    // Gems Shop
    public ConfigurationSection GEM_SHOP_CATEGORIES;

    public Menus() {
        loadConfig();
    }

    public void loadConfig() {
        // Gems Shop
        GEM_SHOP_CATEGORIES = SimpleGems.getInstance().getMenusFile().getConfigurationSection("categories");
    }
}