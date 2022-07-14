package me.refracdevelopment.simplegems.plugin.utilities.files;

import org.bukkit.Material;

import java.util.List;
import java.util.Objects;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class Config {

    // AutoSave Timer
    public static boolean AUTO_SAVE_ENABLED;
    public static long AUTO_SAVE_INTERVAL;

    // Database
    public static String DATA_TYPE;

    // Gems Top
    public static String GEMS_TOP_TITLE;
    public static String GEMS_TOP_FORMAT;
    public static int GEMS_TOP_ENTRIES;

    // Gems Item
    public static Material GEMS_ITEM;
    public static int GEMS_ITEM_DATA;
    public static String GEMS_ITEM_NAME;
    public static List<String> GEMS_ITEM_LORE;

    public static void loadConfig() {
        // AutoSave Timer
        AUTO_SAVE_ENABLED = Files.getConfig().getBoolean("save");
        AUTO_SAVE_INTERVAL = Files.getConfig().getLong("save-interval");

        // Database
        DATA_TYPE = Files.getConfig().getString("data-type");

        // Gems Top
        GEMS_TOP_TITLE = Files.getConfig().getString("gems-top.title");
        GEMS_TOP_FORMAT = Files.getConfig().getString("gems-top.format");
        GEMS_TOP_ENTRIES = Files.getConfig().getInt("gems-top.top-entries");

        // Gems Item
        GEMS_ITEM = Material.getMaterial(Objects.requireNonNull(Files.getConfig().getString("gems-item.material")));
        GEMS_ITEM_DATA = Files.getConfig().getInt("gems-item.data");
        GEMS_ITEM_NAME = Files.getConfig().getString("gems-item.name");
        GEMS_ITEM_LORE = Files.getConfig().getStringList("gems-item.lore");
    }
}