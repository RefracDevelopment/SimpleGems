package me.refracdevelopment.simplegems.utilities.config;

import me.refracdevelopment.simplegems.manager.ConfigurationManager;

import java.util.List;
import java.util.Objects;

public class Config {
    // Settings
    public static long STARTING_GEMS;
    public static int LEADERBOARD_UPDATE_INTERVAL;
    public static String DATA_TYPE;

    // Top
    public static String GEMS_TOP_TITLE;
    public static String GEMS_TOP_FORMAT;
    public static int GEMS_TOP_ENTRIES;

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
        STARTING_GEMS = ConfigurationManager.Setting.STARTING_GEMS.getLong();
        LEADERBOARD_UPDATE_INTERVAL = ConfigurationManager.Setting.LEADERBOARD_UPDATE_INTERVAL.getInt();
        DATA_TYPE = ConfigurationManager.Setting.DATA_TYPE.getString();

        // Top
        GEMS_TOP_TITLE = ConfigurationManager.Setting.GEMS_TOP_TITLE.getString();
        GEMS_TOP_FORMAT = ConfigurationManager.Setting.GEMS_TOP_FORMAT.getString();
        GEMS_TOP_ENTRIES = ConfigurationManager.Setting.GEMS_TOP_ENTRIES.getInt();

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