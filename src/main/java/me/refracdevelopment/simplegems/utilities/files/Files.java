package me.refracdevelopment.simplegems.utilities.files;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Files extends Manager {

    private static File menusFile;
    private static FileConfiguration menus;

    private static File dataFile;
    private static FileConfiguration data;

    public Files(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    public void loadFiles() {
        if (!rosePlugin.getDataFolder().exists()) {
            this.rosePlugin.getDataFolder().mkdirs();
        }

        menusFile = new File(rosePlugin.getDataFolder(), "menus.yml");
        if (!menusFile.exists()) {
            this.rosePlugin.saveResource("menus.yml", false);
        }
        menus = YamlConfiguration.loadConfiguration(menusFile);

        dataFile = new File(rosePlugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            this.rosePlugin.saveResource("data.yml", false);
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

    public void saveData() {
        try {
            data.save(dataFile);
        } catch (Exception exception) {
            Color.log("&cFailed to save the data file!");
            exception.printStackTrace();
        }
    }

    public void reloadFiles() {
        menusFile = new File(this.rosePlugin.getDataFolder(), "menus.yml");
        try {
            menus = YamlConfiguration.loadConfiguration(menusFile);
        } catch (Exception exception) {
            Color.log("&cFailed to reload the menus file!");
            exception.printStackTrace();
        }

        dataFile = new File(this.rosePlugin.getDataFolder(), "data.yml");
        try {
            data = YamlConfiguration.loadConfiguration(dataFile);
        } catch (Exception exception) {
            Color.log("&cFailed to reload the data file!");
            exception.printStackTrace();
        }

        Config.loadConfig();
        Menus.loadMenus();
    }

    @Override
    public void reload() {
        reloadFiles();
    }

    @Override
    public void disable() {

    }
}