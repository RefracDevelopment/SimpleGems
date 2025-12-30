package me.refracdevelopment.simplegems.managers.configuration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigFile extends YamlConfiguration {

    private File file;
    private final JavaPlugin plugin;
    private final String name;

    public ConfigFile(JavaPlugin plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name);
        this.plugin = plugin;
        this.name = name;

        if (!this.file.exists()) {
            plugin.saveResource(name, false);
        }

        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        this.file = new File(plugin.getDataFolder(), name);

        if (!this.file.exists()) {
            plugin.saveResource(name, false);
        }
        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        load();
    }

    @Override
    public int getInt(String path) {
        return super.getInt(path, 0);
    }

    @Override
    public double getDouble(String path) {
        return super.getDouble(path, 0.0);
    }

    @Override
    public boolean getBoolean(String path) {
        return super.getBoolean(path, true);
    }

    public String getString(String path, boolean check) {
        return super.getString(path, "String at path '" + path + "' not found.").replace("|", "\u2503");
    }

    @Override
    public String getString(String path) {
        return super.getString(path, "String at path '" + path + "' not found.").replace("|", "\u2503");
    }

    @Override
    public List<String> getStringList(String path) {
        return super.getStringList(path).stream().map(String::new).collect(Collectors.toList());
    }

    public List<String> getStringList(String path, boolean check) {
        if (!super.contains(path)) return null;
        return super.getStringList(path).stream().map(String::new).collect(Collectors.toList());
    }

    public boolean getOption(String option) {
        return this.getBoolean("options." + option);
    }

}