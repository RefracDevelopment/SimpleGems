package me.refracdevelopment.simplegems.plugin.utilities.files;

import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.utilities.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class Files {

    private static File configFile;
    private static FileConfiguration config;

    private static File messagesFile;
    private static FileConfiguration messages;

    private static File menusFile;
    private static FileConfiguration menus;

    private static File dataFile;
    private static FileConfiguration data;

    public static void loadFiles(SimpleGems plugin) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        menusFile = new File(plugin.getDataFolder(), "menus.yml");
        if (!menusFile.exists()) {
            plugin.saveResource("menus.yml", false);
        }
        menus = YamlConfiguration.loadConfiguration(menusFile);

        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            plugin.saveResource("data.yml", false);
        }
        data = YamlConfiguration.loadConfiguration(dataFile);

        Config.loadConfig();
        Menus.loadMenus();
        Messages.loadMessages();

        Logger.NONE.out("&c==========================================");
        Logger.NONE.out("&aAll files have been loaded correctly!");
        Logger.NONE.out("&c==========================================");
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static FileConfiguration getMessages() {
        return messages;
    }

    public static FileConfiguration getMenus() {
        return menus;
    }

    public static FileConfiguration getData() {
        return data;
    }

    public static void saveData() {
        try {
            data.save(dataFile);
        } catch (Exception e) {
            Logger.ERROR.out("Failed to save the data file!");
            e.printStackTrace();
        }
    }

    public static void reloadFiles(SimpleGems plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            Logger.ERROR.out("Failed to reload the config file!");
            e.printStackTrace();
        }

        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        try {
            messages = YamlConfiguration.loadConfiguration(messagesFile);
        } catch (Exception e) {
            Logger.ERROR.out("Failed to reload the messages file!");
            e.printStackTrace();
        }

        menusFile = new File(plugin.getDataFolder(), "menus.yml");
        try {
            menus = YamlConfiguration.loadConfiguration(menusFile);
        } catch (Exception e) {
            Logger.ERROR.out("Failed to reload the menus file!");
            e.printStackTrace();
        }

        dataFile = new File(plugin.getDataFolder(), "data.yml");
        try {
            data = YamlConfiguration.loadConfiguration(dataFile);
        } catch (Exception e) {
            Logger.ERROR.out("Failed to reload the data file!");
            e.printStackTrace();
        }

        Config.loadConfig();
        Menus.loadMenus();
        Messages.loadMessages();

        Logger.NONE.out("&c==========================================");
        Logger.NONE.out("&aAll files have been reloaded correctly!");
        Logger.NONE.out("&c==========================================");
    }
}