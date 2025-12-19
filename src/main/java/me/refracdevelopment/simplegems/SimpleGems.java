package me.refracdevelopment.simplegems;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tcoded.folialib.FoliaLib;
import lombok.Getter;
import lombok.Setter;
import me.gabytm.util.actions.ActionManager;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.commands.*;
import me.refracdevelopment.simplegems.hooks.*;
import me.refracdevelopment.simplegems.listeners.*;
import me.refracdevelopment.simplegems.managers.*;
import me.refracdevelopment.simplegems.managers.configuration.*;
import me.refracdevelopment.simplegems.managers.configuration.cache.*;
import me.refracdevelopment.simplegems.managers.data.*;
import me.refracdevelopment.simplegems.managers.leaderboards.*;
import me.refracdevelopment.simplegems.menu.*;
import me.refracdevelopment.simplegems.utilities.*;
import me.refracdevelopment.simplegems.utilities.chat.*;
import me.refracdevelopment.simplegems.utilities.command.*;
import me.refracdevelopment.simplegems.utilities.menu.*;
import me.refracdevelopment.simplegems.utilities.menu.actions.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Getter
@Setter
public final class SimpleGems extends JavaPlugin {

    @Getter
    private static SimpleGems instance;

    // Managers
    private DataType dataType;
    private MySQLManager mySQLManager;
    private SQLiteManager sqLiteManager;
    private ProfileManager profileManager;
    private ActionManager actionManager;
    private LeaderboardManager leaderboardManager;

    // Menus
    private GemShop gemShop;

    // Files
    private ConfigFile configFile;
    private ConfigFile commandsFile;
    private ConfigFile menusFile;
    private ConfigFile localeFile;
    private Locale locale;

    // Cache
    private Config settings;
    private Commands commands;
    private Menus menus;

    // Utilities
    private SimpleGemsAPI gemsAPI;
    private FoliaLib foliaLib;
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        foliaLib = new FoliaLib(this);
        adventure = BukkitAudiences.create(this);

        DownloadUtil.downloadAndEnable(this);

        loadFiles();

        RyMessageUtils.sendConsole(false,
                "<#A020F0> _____ _           _     _____                 " + "Running <#7D0DC3>v" + getDescription().getVersion(),
                "<#A020F0>|   __|_|_____ ___| |___|   __|___ _____ ___   " + "Server <#7D0DC3>" + getServer().getName() + " <#A020F0>v<#7D0DC3>" + getServer().getVersion(),
                "<#A020F0>|__   | |     | . | | -_|  |  | -_|     |_ -|  " + "Discord support: <#7D0DC3>" + getDescription().getWebsite(),
                "<#A020F0>|_____|_|_|_|_|  _|_|___|_____|___|_|_|_|___|  " + "Thanks for using my plugin ❤ !",
                "<#A020F0>              |_|                            ",
                "        <#A020F0>Developed by <#7D0DC3>RefracDevelopment",
                ""
        );

        loadManagers();
        loadCommands();
        loadListeners();
        loadHooks();

        updateCheck(true);

        new Metrics(this, 13117);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            if (dataType == DataType.MYSQL)
                getMySQLManager().shutdown();
            else if (dataType == DataType.SQLITE)
                getSqLiteManager().shutdown();

            getFoliaLib().getScheduler().cancelAllTasks();

            if (this.adventure != null) {
                this.adventure.close();
                this.adventure = null;
            }
        } catch (Exception ignored) {
        }
    }

    private void loadFiles() {
        // Files
        configFile = new ConfigFile(this, "config.yml");
        menusFile = new ConfigFile(this, "menus.yml");
        commandsFile = new ConfigFile(this, "commands/gems.yml");

        // Cache
        locale = new Locale();
        settings = new Config();
        menus = new Menus();
        commands = new Commands();

        RyMessageUtils.sendConsole(true, "&aLoaded all files.");
    }

    private void loadManagers() {
        // Setup database
        switch (getSettings().DATA_TYPE.toUpperCase()) {
            case "MARIADB":
            case "MYSQL":
                dataType = DataType.MYSQL;
                mySQLManager = new MySQLManager();
                break;
            default:
                dataType = DataType.SQLITE;
                sqLiteManager = new SQLiteManager(getDataFolder().getAbsolutePath() + File.separator + "gems.db");
                break;
        }

        profileManager = new ProfileManager();
        gemsAPI = new SimpleGemsAPI();
        actionManager = new ActionManager(this);

        // Setup menus
        MenuManager.setup(getServer(), this);

        // Register custom actions
        actionManager.register(new BackAction(), true);
        actionManager.register(new MenuAction(), true);

        leaderboardManager = new LeaderboardManager();

        RyMessageUtils.sendConsole(true, "&aLoaded managers.");
    }

    private void loadCommands() {
        try {
            CommandManager.createCoreCommand(this, commands.GEMS_COMMAND_NAME,
                    localeFile.getString("command-help-description"),
                    "/" + commands.GEMS_COMMAND_NAME,
                    (commandSender, list) -> {
                        if (!(commandSender instanceof Player player)) {
                            list.forEach(command -> {
                                StringPlaceholders placeholders;

                                if (!command.getSyntax().isEmpty()) {
                                    placeholders = StringPlaceholders.builder()
                                            .add("cmd", SimpleGems.getInstance().getCommands().GEMS_COMMAND_NAME)
                                            .add("subcmd", command.getName())
                                            .add("args", command.getSyntax())
                                            .add("desc", command.getDescription())
                                            .build();
                                    RyMessageUtils.sendPluginMessage(commandSender, "command-help-list-description", placeholders);
                                } else {
                                    placeholders = StringPlaceholders.builder()
                                            .add("cmd", SimpleGems.getInstance().getCommands().GEMS_COMMAND_NAME)
                                            .add("subcmd", command.getName())
                                            .add("desc", command.getDescription())
                                            .build();
                                    RyMessageUtils.sendPluginMessage(commandSender, "command-help-list-description-no-args", placeholders);
                                }
                            });
                            return;
                        }

                        settings.GEMS_BALANCE.forEach(message ->
                                RyMessageUtils.sendPlayer(player, message)
                        );
                    },
                    commands.GEMS_COMMAND_ALIASES,
                    HelpCommand.class,
                    BalanceCommand.class,
                    TopCommand.class,
                    ShopCommand.class,
                    WithdrawCommand.class,
                    PayCommand.class,
                    GiveCommand.class,
                    TakeCommand.class,
                    SetCommand.class,
                    ReloadCommand.class,
                    VersionCommand.class,
                    RandomGiveCommand.class
            );
        } catch (NoSuchFieldException | IllegalAccessException e) {
            RyMessageUtils.sendPluginError("&cFailed to load commands.", e, true, true);
            e.printStackTrace();
            return;
        }

        RyMessageUtils.sendConsole(true, "&aLoaded commands.");
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // Loads all available menu data
        if (getServer().getPluginManager().isPluginEnabled("ItemsAdder"))
            // Wait for ItemsAdder custom items to be loaded first
            getServer().getPluginManager().registerEvents(new ItemsAdderListener(), this);
        else
            gemShop = new GemShop();

        RyMessageUtils.sendConsole(true, "&aLoaded listeners.");
    }

    private void loadHooks() {
        PluginManager pluginManager = getServer().getPluginManager();

        if (pluginManager.isPluginEnabled("Skulls")) {
            RyMessageUtils.sendConsole(true, "&aHooked into Skulls for heads support.");
        }

        if (pluginManager.isPluginEnabled("HeadDatabase")) {
            RyMessageUtils.sendConsole(true, "&aHooked into HeadDatabase for heads support.");
        }

        if (pluginManager.isPluginEnabled("ItemsAdder")) {
            RyMessageUtils.sendConsole(true, "&aHooked into ItemsAdder for custom items support.");
        }

        if (pluginManager.isPluginEnabled("PlaceholderAPI")) {
            new PAPIExpansion().register();
            RyMessageUtils.sendConsole(true, "&aHooked into PlaceholderAPI for placeholders.");
        }
    }

    public boolean updateCheck(boolean sendMessages) {
        try {
            String urlString = "https://refracdev-updatecheck.refracdev.workers.dev/";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            StringBuilder response = new StringBuilder();
            while ((input = reader.readLine()) != null) {
                response.append(input);
            }
            reader.close();
            JsonObject object = new JsonParser().parse(response.toString()).getAsJsonObject();

            if (object.has("plugins")) {
                JsonObject plugins = object.get("plugins").getAsJsonObject();
                JsonObject info = plugins.get(getDescription().getName()).getAsJsonObject();
                String version = info.get("version").getAsString();
                boolean archived = info.get("archived").getAsBoolean();

                if (archived) {
                    if (sendMessages) {
                        RyMessageUtils.sendConsole(true, "&cThis plugin has been marked as &e&l'Archived' &cby RefracDevelopment.");
                        RyMessageUtils.sendConsole(true, "&cThis version will continue to work but will not receive updates or support.");
                    }
                } else if (version.equals(getDescription().getVersion())) {
                    if (sendMessages) {
                        RyMessageUtils.sendConsole(true, "&a" + getDescription().getName() + " is on the latest version.");
                    }
                } else {
                    if (sendMessages)
                        RyMessageUtils.sendConsole(true, "&cYour " + getDescription().getName() + " version &7(" + getDescription().getVersion() + ") &cis out of date! Newest: &e&lv" + version);
                    return true;
                }
            } else {
                if (sendMessages)
                    RyMessageUtils.sendConsole(true, "&cWrong response from update API, contact plugin developer!");
            }
        } catch (
                Exception ex) {
            RyMessageUtils.sendConsole(true, "&cFailed to get updater check. (" + ex.getMessage() + ")");
        }

        return false;
    }
}