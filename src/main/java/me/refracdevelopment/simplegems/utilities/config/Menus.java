package me.refracdevelopment.simplegems.utilities.config;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Utilities;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class Menus {

    // Gems Shop
    public static boolean GEM_SHOP_ENABLED;
    public static String GEM_SHOP_TITLE;
    public static int GEM_SHOP_SIZE;
    public static Material GEM_SHOP_FILL_MATERIAL;
    public static int GEM_SHOP_FILL_DATA;
    public static String GEM_SHOP_FILL_NAME;
    public static ConfigurationSection GEM_SHOP_ITEMS;

    public static void loadMenus(SimpleGems plugin) {
        // Gems Shop
        GEM_SHOP_ENABLED = plugin.getMenusFile().getBoolean("gems-menu.enabled");
        GEM_SHOP_TITLE = plugin.getMenusFile().getString("gems-menu.title");
        GEM_SHOP_SIZE = plugin.getMenusFile().getInt("gems-menu.size");
        GEM_SHOP_FILL_MATERIAL = Utilities.getMaterial(Objects.requireNonNull(plugin.getMenusFile().getString("gems-menu.fill.material"))).parseMaterial();
        GEM_SHOP_FILL_DATA = plugin.getMenusFile().getInt("gems-menu.fill.data");
        GEM_SHOP_FILL_NAME = plugin.getMenusFile().getString("gems-menu.fill.name");
        GEM_SHOP_ITEMS = plugin.getMenusFile().getConfigurationSection("gems-menu.items");
    }
}