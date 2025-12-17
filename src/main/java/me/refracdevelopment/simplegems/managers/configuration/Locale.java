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
        SimpleGems instance = SimpleGems.getInstance();

        for (LocaleType locale : LocaleType.values()) {
            File file = new File(instance.getDataFolder(), "locale/" + locale.toString() + ".yml");

            if (!file.exists()) {
                instance.saveResource("locale/" + locale + ".yml", false);
            }
        }

        instance.setLocaleFile(new ConfigFile(instance, "locale/" + instance.getConfigFile().getString("locale", "en_US") + ".yml"));
    }
}
