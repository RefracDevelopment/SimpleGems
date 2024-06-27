package me.refracdevelopment.simplegems.managers.configuration.cache;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import me.refracdevelopment.simplegems.SimpleGems;

public class Menus {

    // Gems Shop
    public Section GEM_SHOP_CATEGORIES;

    public Menus() {
        loadConfig();
    }

    public void loadConfig() {
        // Gems Shop
        GEM_SHOP_CATEGORIES = SimpleGems.getInstance().getMenusFile().getSection("categories");
    }
}