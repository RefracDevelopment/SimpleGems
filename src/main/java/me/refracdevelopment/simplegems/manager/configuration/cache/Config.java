package me.refracdevelopment.simplegems.manager.configuration.cache;

import me.refracdevelopment.simplegems.SimpleGems;

import java.util.List;

public class Config {

    // Settings
    public long STARTING_GEMS;
    public int LEADERBOARD_UPDATE_INTERVAL;
    public String DATA_TYPE;

    // Top
    public String GEMS_TOP_TITLE;
    public String GEMS_TOP_FORMAT;
    public int GEMS_TOP_ENTRIES;

    // Messages
    public List<String> GEMS_BALANCE;

    // Gems Item
    public String GEMS_ITEM_MATERIAL;
    public int GEMS_ITEM_DATA;
    public boolean GEMS_ITEM_CUSTOM_DATA;
    public int GEMS_ITEM_CUSTOM_MODEL_DATA;
    public String GEMS_ITEM_NAME;
    public boolean GEMS_ITEM_GLOW;
    public List<String> GEMS_ITEM_LORE;

    public Config() {
        loadConfig();
    }

    public void loadConfig() {
        // Settings
        STARTING_GEMS = SimpleGems.getInstance().getConfigFile().getInt("starting-gems");
        LEADERBOARD_UPDATE_INTERVAL = SimpleGems.getInstance().getConfigFile().getInt("leaderboard-update-interval");
        DATA_TYPE = SimpleGems.getInstance().getConfigFile().getString("data-type");

        // Top
        GEMS_TOP_TITLE = SimpleGems.getInstance().getConfigFile().getString("gems-top.title");
        GEMS_TOP_FORMAT = SimpleGems.getInstance().getConfigFile().getString("gems-top.format");
        GEMS_TOP_ENTRIES = SimpleGems.getInstance().getConfigFile().getInt("gems-top.top-entries");

        // Messages
        GEMS_BALANCE = SimpleGems.getInstance().getConfigFile().getStringList("gems-balance");

        // Gems Item
        GEMS_ITEM_MATERIAL = SimpleGems.getInstance().getConfigFile().getString("gems-item.material");
        GEMS_ITEM_DATA = SimpleGems.getInstance().getConfigFile().getInt("gems-item.data");
        GEMS_ITEM_CUSTOM_DATA = SimpleGems.getInstance().getConfigFile().getBoolean("gems-item.custom-data");
        GEMS_ITEM_CUSTOM_MODEL_DATA = SimpleGems.getInstance().getConfigFile().getInt("gems-item.custom-model-data");
        GEMS_ITEM_NAME = SimpleGems.getInstance().getConfigFile().getString("gems-item.name");
        GEMS_ITEM_GLOW = SimpleGems.getInstance().getConfigFile().getBoolean("gems-item.glow");
        GEMS_ITEM_LORE = SimpleGems.getInstance().getConfigFile().getStringList("gems-item.lore");
    }
}