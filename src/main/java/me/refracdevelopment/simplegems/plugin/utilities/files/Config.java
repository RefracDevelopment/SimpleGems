package me.refracdevelopment.simplegems.plugin.utilities.files;

import org.bukkit.Material;

import java.util.List;
import java.util.Objects;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class Config {
    // Database
    public static String DATA_TYPE;

    // Gems Top
    public static String GEMS_TOP_TITLE;
    public static String GEMS_TOP_FORMAT;
    public static int GEMS_TOP_ENTRIES;

    // Gems Item
    public static String GEMS_ITEM;
    public static int GEMS_ITEM_DATA;
    public static boolean GEMS_ITEM_CUSTOM_DATA;
    public static int GEMS_ITEM_CUSTOM_MODEL_DATA;
    public static String GEMS_ITEM_NAME;
    public static boolean GEMS_ITEM_GLOW;
    public static List<String> GEMS_ITEM_LORE;

    // Commands
    public static boolean CONSOLE_GEMS_COMMAND_ENABLED;
    public static String CONSOLE_GEMS_COMMAND;
    public static List<String> CONSOLE_GEMS_ALIASES;
    public static boolean GEMS_COMMAND_ENABLED;
    public static String GEMS_COMMAND;
    public static List<String> GEMS_ALIASES;
    public static boolean GEMS_SHOP_COMMAND_ENABLED;
    public static String GEMS_SHOP_COMMAND;
    public static List<String> GEMS_SHOP_ALIASES;
    public static boolean GEMS_RELOAD_COMMAND_ENABLED;
    public static String GEMS_RELOAD_COMMAND;
    public static List<String> GEMS_RELOAD_ALIASES;
    public static boolean GEMS_TOP_COMMAND_ENABLED;
    public static String GEMS_TOP_COMMAND;
    public static List<String> GEMS_TOP_ALIASES;

    public static void loadConfig() {
        // Database
        DATA_TYPE = Files.getConfig().getString("data-type");

        // Gems Top
        GEMS_TOP_TITLE = Files.getConfig().getString("gems-top.title");
        GEMS_TOP_FORMAT = Files.getConfig().getString("gems-top.format");
        GEMS_TOP_ENTRIES = Files.getConfig().getInt("gems-top.top-entries");

        // Gems Item
        GEMS_ITEM = Objects.requireNonNull(Files.getConfig().getString("gems-item.material"));
        GEMS_ITEM_DATA = Files.getConfig().getInt("gems-item.data");
        GEMS_ITEM_CUSTOM_DATA = Files.getConfig().getBoolean("gems-item.custom-data");
        GEMS_ITEM_CUSTOM_MODEL_DATA = Files.getConfig().getInt("gems-item.custom-model-data");
        GEMS_ITEM_NAME = Files.getConfig().getString("gems-item.name");
        GEMS_ITEM_GLOW = Files.getConfig().getBoolean("gems-item.glow");
        GEMS_ITEM_LORE = Files.getConfig().getStringList("gems-item.lore");

        // Commands
        CONSOLE_GEMS_COMMAND_ENABLED = Files.getConfig().getBoolean("commands.console-gems.enabled");
        CONSOLE_GEMS_COMMAND = Files.getConfig().getString("commands.console-gems.command");
        CONSOLE_GEMS_ALIASES = Files.getConfig().getStringList("commands.console-gems.aliases");
        GEMS_COMMAND_ENABLED = Files.getConfig().getBoolean("commands.gems.enabled");
        GEMS_COMMAND = Files.getConfig().getString("commands.gems.command");
        GEMS_ALIASES = Files.getConfig().getStringList("commands.gems.aliases");
        GEMS_SHOP_COMMAND_ENABLED = Files.getConfig().getBoolean("commands.gems-shop.enabled");
        GEMS_SHOP_COMMAND = Files.getConfig().getString("commands.gems-shop.command");
        GEMS_SHOP_ALIASES = Files.getConfig().getStringList("commands.gems-shop.aliases");
        GEMS_RELOAD_COMMAND_ENABLED = Files.getConfig().getBoolean("commands.gems-reload.enabled");
        GEMS_RELOAD_COMMAND = Files.getConfig().getString("commands.gems-reload.command");
        GEMS_RELOAD_ALIASES = Files.getConfig().getStringList("commands.gems-reload.aliases");
        GEMS_TOP_COMMAND_ENABLED = Files.getConfig().getBoolean("commands.gems-top.enabled");
        GEMS_TOP_COMMAND = Files.getConfig().getString("commands.gems-top.command");
        GEMS_TOP_ALIASES = Files.getConfig().getStringList("commands.gems-top.aliases");
    }
}