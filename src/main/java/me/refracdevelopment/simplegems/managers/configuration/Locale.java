package me.refracdevelopment.simplegems.managers.configuration;

import me.refracdevelopment.simplegems.SimpleGems;

import java.io.File;

public class Locale {

    private enum LocaleType {
        en_US
    }

    public Locale() {
        load();
    }

    public void load() {
        SimpleGems plugin = SimpleGems.getInstance();

        for (LocaleType locale : LocaleType.values()) {
            File file = new File(plugin.getDataFolder(), "locale/" + locale.toString() + ".yml");

            if (!file.exists()) {
                plugin.saveResource("locale/" + locale + ".yml", false);
            }
        }

        plugin.setLocaleFile(new ConfigFile(plugin, "locale/" + plugin.getConfigFile().getString("locale", "en_US") + ".yml"));
    }
}
