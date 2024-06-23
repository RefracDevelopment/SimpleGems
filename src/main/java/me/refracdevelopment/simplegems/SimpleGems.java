package me.refracdevelopment.simplegems;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import me.gabytm.util.actions.ActionManager;
import me.kodysimpson.simpapi.command.CommandList;
import me.kodysimpson.simpapi.command.CommandManager;
import me.kodysimpson.simpapi.command.SubCommand;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.commands.*;
import me.refracdevelopment.simplegems.hooks.ItemsAdderListener;
import me.refracdevelopment.simplegems.listeners.PlayerListener;
import me.refracdevelopment.simplegems.manager.ProfileManager;
import me.refracdevelopment.simplegems.manager.configuration.ConfigFile;
import me.refracdevelopment.simplegems.manager.configuration.cache.Commands;
import me.refracdevelopment.simplegems.manager.configuration.cache.Config;
import me.refracdevelopment.simplegems.manager.configuration.cache.Menus;
import me.refracdevelopment.simplegems.manager.data.DataType;
import me.refracdevelopment.simplegems.manager.data.MySQLManager;
import me.refracdevelopment.simplegems.manager.data.SQLiteManager;
import me.refracdevelopment.simplegems.manager.leaderboards.LeaderboardManager;
import me.refracdevelopment.simplegems.menu.GemShop;
import me.refracdevelopment.simplegems.utilities.DownloadUtil;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.PAPIExpansion;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.chat.StringPlaceholders;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import space.arim.morepaperlib.MorePaperLib;
import space.arim.morepaperlib.adventure.MorePaperLibAdventure;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

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

    // Cache
    private Config settings;
    private Commands commands;
    private Menus menus;

    // Utilities
    private DownloadUtil downloadUtil;
    private SimpleGemsAPI gemsAPI;
    private MorePaperLib paperLib;
    private MorePaperLibAdventure paperLibAdventure;
    private List<SubCommand> commandsList;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        paperLib = new MorePaperLib(this);
        paperLibAdventure = new MorePaperLibAdventure(paperLib);

        DownloadUtil.downloadAndEnable(this);

        loadFiles();

        RyMessageUtils.sendConsole(false,
                "<#A020F0> _____ _           _     _____                 " + "Running <#7D0DC3>v" + getDescription().getVersion(),
                "<#A020F0>|   __|_|_____ ___| |___|   __|___ _____ ___   " + "Server <#7D0DC3>" + getServer().getName() + " <#A020F0>v" + getServer().getVersion(),
                "<#A020F0>|__   | |     | . | | -_|  |  | -_|     |_ -|  " + "Discord support: <#7D0DC3>" + getDescription().getWebsite(),
                "<#7D0DC3>|_____|_|_|_|_|  _|_|___|_____|___|_|_|_|___|  " + "Thanks for using my plugin ❤ !",
                "<#7D0DC3>              |_|                            ",
                "        <#A020F0>Developed by <#7D0DC3>RefracDevelopment",
                ""
        );

        loadManagers();
        loadCommands();
        loadListeners();
        loadHooks();

        Tasks.runAsync(this::updateCheck);

        new Metrics(this, 13117);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (Objects.requireNonNull(dataType) == DataType.MYSQL)
            getMySQLManager().shutdown();
        else if (Objects.requireNonNull(dataType) == DataType.SQLITE)
            getSqLiteManager().shutdown();

        paperLib.scheduling().cancelGlobalTasks();
    }

    private void loadFiles() {
        // Files
        configFile = new ConfigFile("config.yml", true);
        menusFile = new ConfigFile("menus.yml", false);
        commandsFile = new ConfigFile("commands/gems.yml", true);
        localeFile = new ConfigFile("locale/" + getConfigFile().getString("locale", "en_US") + ".yml", true);

        // Cache
        settings = new Config();
        menus = new Menus();
        commands = new Commands();

        RyMessageUtils.sendConsole(true, "&aLoaded all files.");
    }

    private void loadManagers() {
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

        gemsAPI = new SimpleGemsAPI();
        profileManager = new ProfileManager();
        actionManager = new ActionManager(this);
        leaderboardManager = new LeaderboardManager();
        MenuManager.setup(getServer(), this);

        RyMessageUtils.sendConsole(true, "&aLoaded managers.");
    }

    private void loadCommands() {
        try {
            CommandManager.createCoreCommand(this, getCommands().GEMS_COMMAND_NAME,
                    getLocaleFile().getString("command-help-description"),
                    "/" + getCommands().GEMS_COMMAND_NAME,
                    new CommandList() {
                        @Override
                        public void displayCommandList(CommandSender commandSender, List<SubCommand> list) {
                            commandsList = list;

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

                            getSettings().GEMS_BALANCE.forEach(message ->
                                    RyMessageUtils.sendPlayer(player, message)
                            );
                        }
                    },
                    getCommands().GEMS_COMMAND_ALIASES,
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
                    UpdateCommand.class,
                    ResetCommand.class,
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

        if (getServer().getPluginManager().isPluginEnabled("ItemsAdder"))
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

    public void updateCheck() {
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
                    RyMessageUtils.sendConsole(true, "&cThis plugin has been marked as &e&l'Archived' &cby RefracDevelopment.");
                    RyMessageUtils.sendConsole(true, "&cThis version will continue to work but will not receive updates or support.");
                } else if (version.equals(getDescription().getVersion())) {
                    RyMessageUtils.sendConsole(true, "&a" + getDescription().getName() + " is on the latest version.");
                } else {
                    RyMessageUtils.sendConsole(true, "&cYour " + getDescription().getName() + " version &7(" + getDescription().getVersion() + ") &cis out of date! Newest: &e&lv" + version);
                }
            } else {
                RyMessageUtils.sendConsole(true, "&cWrong response from update API, contact plugin developer!");
            }
        } catch (
                Exception ex) {
            RyMessageUtils.sendConsole(true, "&cFailed to get updater check. (" + ex.getMessage() + ")");
        }
    }
}