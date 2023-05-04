package me.refracdevelopment.simplegems.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager;
import me.refracdevelopment.simplegems.SimpleGems;

import java.util.Arrays;

public class ConfigurationManager extends AbstractConfigurationManager {

    public enum Setting implements RoseSetting {
        // Config Settings
        STARTING_GEMS("starting-gems", 100, "This is used to allow players to start with", "a defined amount of gems on first join"),
        GEMS_BALANCE("gems-balance", Arrays.asList(
                "",
                "&e&l&eYou currently have &f&n%gems_formatted%&e gems!",
                "&7&o(( &f&oTip&7&o: You can get gems by doing &e&oevents&7&o! ))",
                ""
        )),
        GEMS_ITEM_MATERIAL("gems-item.material", "DIAMOND", "This is used for withdrawing gems"),
        GEMS_ITEM_DATA("gems-item.data", 0),
        GEMS_ITEM_CUSTOM_DATA("gems-item.customData", false),
        GEMS_ITEM_CUSTOM_MODEL_DATA("gems-item.customModelData", 0),
        GEMS_ITEM_NAME("gems-item.name", "&e&lGems &7(Right Click)"),
        GEMS_ITEM_GLOW("gems-item.glow", false),
        GEMS_ITEM_LORE("gems-item.lore", Arrays.asList(
                "&7&oRight click to redeem these gems you",
                "&7&ohave obtained through your journey"
        ))
        ;

        private final String key;
        private final Object defaultValue;
        private final String[] comments;
        private Object value = null;

        Setting(String key, Object defaultValue, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public Object getDefaultValue() {
            return this.defaultValue;
        }

        @Override
        public String[] getComments() {
            return this.comments;
        }

        @Override
        public Object getCachedValue() {
            return this.value;
        }

        @Override
        public void setCachedValue(Object value) {
            this.value = value;
        }

        @Override
        public CommentedFileConfiguration getBaseConfig() {
            return SimpleGems.getInstance().getManager(ConfigurationManager.class).getConfig();
        }
    }

    public ConfigurationManager(RosePlugin rosePlugin) {
        super(rosePlugin, Setting.class);
    }

    @Override
    protected String[] getHeader() {
        return new String[]{
                "  ___________                __           ________                       ",
                " /   _____/__| _____ ______ |  |   ____  /  _____/  ____   _____   ______",
                " \\_____  \\|  |/     \\\\____ \\|  | _/ __ \\/   \\  ____/ __ \\ /     \\ /  ___/",
                " /        \\  |  | |  \\  |_\\ \\  |__  ___/_    \\_\\  \\  ___/_  | |  \\\\___ \\ ",
                "/_______  /__|__|_|  /   ___/____/\\___  /\\______  /\\___  /__|_|  /____  \\",
                "        \\/         \\/|__|             \\/        \\/     \\/      \\/     \\/ "
        };
    }

}