package me.refracdevelopment.simplegems.utilities.config;

import me.refracdevelopment.simplegems.manager.ConfigurationManager;

import java.util.List;
import java.util.Objects;

public class Config {
    // Settings
    public static double STARTING_GEMS;

    // Messages
    public static List<String> GEMS_BALANCE;

    // Gems Item
    public static String GEMS_ITEM_MATERIAL;
    public static int GEMS_ITEM_DATA;
    public static boolean GEMS_ITEM_CUSTOM_DATA;
    public static int GEMS_ITEM_CUSTOM_MODEL_DATA;
    public static String GEMS_ITEM_NAME;
    public static boolean GEMS_ITEM_GLOW;
    public static List<String> GEMS_ITEM_LORE;

    public static void loadConfig() {
        // Settings
        STARTING_GEMS = ConfigurationManager.Setting.STARTING_GEMS.getDouble();

        // Messages
        GEMS_BALANCE = ConfigurationManager.Setting.GEMS_BALANCE.getStringList();

        // Gems Item
        GEMS_ITEM_MATERIAL = Objects.requireNonNull(ConfigurationManager.Setting.GEMS_ITEM_MATERIAL.getString());
        GEMS_ITEM_DATA = ConfigurationManager.Setting.GEMS_ITEM_DATA.getInt();
        GEMS_ITEM_CUSTOM_DATA = ConfigurationManager.Setting.GEMS_ITEM_CUSTOM_DATA.getBoolean();
        GEMS_ITEM_CUSTOM_MODEL_DATA = ConfigurationManager.Setting.GEMS_ITEM_CUSTOM_MODEL_DATA.getInt();
        GEMS_ITEM_NAME = ConfigurationManager.Setting.GEMS_ITEM_NAME.getString();
        GEMS_ITEM_GLOW = ConfigurationManager.Setting.GEMS_ITEM_GLOW.getBoolean();
        GEMS_ITEM_LORE = ConfigurationManager.Setting.GEMS_ITEM_LORE.getStringList();
    }
}