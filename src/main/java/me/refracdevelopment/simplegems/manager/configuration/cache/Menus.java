package me.refracdevelopment.simplegems.manager.configuration.cache;

import me.refracdevelopment.simplegems.SimpleGems;
import org.bukkit.configuration.ConfigurationSection;

public class Menus {

    // Gems Shop
    public static ConfigurationSection GEM_SHOP_CATEGORIES;

    public static void loadMenus() {
        // Gems Shop
        GEM_SHOP_CATEGORIES = SimpleGems.getInstance().getMenusFile().getConfigurationSection("categories");
    }
}