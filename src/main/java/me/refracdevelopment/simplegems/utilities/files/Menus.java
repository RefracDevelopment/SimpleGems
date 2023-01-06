package me.refracdevelopment.simplegems.utilities.files;

import me.refracdevelopment.simplegems.utilities.Utilities;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Objects;

public class Menus {

    // Gems Shop
    public static boolean GEM_SHOP_ENABLED;
    public static String GEM_SHOP_TITLE;
    public static int GEM_SHOP_SIZE;
    public static Material GEM_SHOP_FILL_MATERIAL;
    public static int GEM_SHOP_FILL_DATA;
    public static String GEM_SHOP_FILL_NAME;
    public static List<String> GEM_SHOP_FILL_LORE;
    public static ConfigurationSection GEM_SHOP_ITEMS;

    public static void loadMenus() {
        // Gems Shop
        GEM_SHOP_ENABLED = Files.getMenus().getBoolean("gems-menu.enabled");
        GEM_SHOP_TITLE = Files.getMenus().getString("gems-menu.title");
        GEM_SHOP_SIZE = Files.getMenus().getInt("gems-menu.size");
        GEM_SHOP_FILL_MATERIAL = Utilities.getMaterial(Objects.requireNonNull(Files.getMenus().getString("gems-menu.fill.material"))).parseMaterial();
        GEM_SHOP_FILL_DATA = Files.getMenus().getInt("gems-menu.fill.data");
        GEM_SHOP_FILL_NAME = Files.getMenus().getString("gems-menu.fill.name");
        GEM_SHOP_FILL_LORE = Files.getMenus().getStringList("gems-menu.fill.lore");
        GEM_SHOP_ITEMS = Files.getMenus().getConfigurationSection("gems-menu.items");
    }
}