package me.refracdevelopment.simplegems.managers.configuration;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigFile {

    private YamlDocument configFile;

    public ConfigFile(String name) {
        try {
            configFile = YamlDocument.create(new File(SimpleGems.getInstance().getDataFolder(), name),
                    getClass().getResourceAsStream("/" + name),
                    GeneralSettings.builder().setUseDefaults(false).build(),
                    LoaderSettings.builder().setAutoUpdate(false).build()
            );

            configFile.update();
            configFile.save();
        } catch (IOException e) {
            RyMessageUtils.sendPluginError("&cFailed to load " + name + " file! The plugin will now shutdown.", e, true, true);
        }
    }

    public void save() {
        try {
            configFile.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            configFile.reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getInt(String path) {
        return configFile.getInt(path, 0);
    }

    public double getDouble(String path) {
        return configFile.getDouble(path, 0.0);
    }

    public long getLong(String path) {
        return configFile.getLong(path, 0L);
    }

    public boolean getBoolean(String path) {
        return configFile.getBoolean(path, false);
    }

    public String getString(String path, Object defined) {
        return configFile.getString(path, ((String) defined));
    }

    public String getString(String path) {
        if (configFile.contains(path)) {
            return configFile.getString(path, "String at path '" + path + "' not found.");
        }

        return null;
    }

    public List<String> getStringList(String path) {
        if (configFile.contains(path))
            return configFile.getStringList(path, List.of("String at path '" + path + "' not found."));

        return null;
    }

    public Section getSection(String path) {
        return configFile.getSection(path);
    }
}