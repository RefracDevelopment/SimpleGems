package me.refracdevelopment.simplegems.utilities.files;

import dev.rosewood.rosegarden.RosePlugin;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Files {

    private static File menusFile;
    private static FileConfiguration menus;

    private static File dataFile;
    private static FileConfiguration data;

    public static void loadFiles(SimpleGems plugin) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

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

        Color.log("&c==========================================");
        Color.log("&aAll files have been loaded correctly!");
        Color.log("&c==========================================");
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
        } catch (Exception exception) {
            Color.log("&cFailed to save the data file!");
            exception.printStackTrace();
        }
    }

    public static void reloadFiles(SimpleGems plugin) {
        menusFile = new File(plugin.getDataFolder(), "menus.yml");
        try {
            menus = YamlConfiguration.loadConfiguration(menusFile);
        } catch (Exception exception) {
            Color.log("&cFailed to reload the menus file!");
            exception.printStackTrace();
        }

        dataFile = new File(plugin.getDataFolder(), "data.yml");
        try {
            data = YamlConfiguration.loadConfiguration(dataFile);
        } catch (Exception exception) {
            Color.log("&cFailed to reload the data file!");
            exception.printStackTrace();
        }

        Config.loadConfig();
        Menus.loadMenus();
    }
}