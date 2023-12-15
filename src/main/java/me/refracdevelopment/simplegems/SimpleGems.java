package me.refracdevelopment.simplegems;

import com.cryptomorin.xseries.ReflectionUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tcoded.folialib.FoliaLib;
import lombok.Getter;
import me.gabytm.util.actions.ActionManager;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.commands.*;
import me.refracdevelopment.simplegems.listeners.MenuListener;
import me.refracdevelopment.simplegems.listeners.PlayerListener;
import me.refracdevelopment.simplegems.manager.CommandManager;
import me.refracdevelopment.simplegems.manager.MenuManager;
import me.refracdevelopment.simplegems.manager.ProfileManager;
import me.refracdevelopment.simplegems.manager.configuration.ConfigFile;
import me.refracdevelopment.simplegems.manager.configuration.cache.Commands;
import me.refracdevelopment.simplegems.manager.configuration.cache.Config;
import me.refracdevelopment.simplegems.manager.configuration.cache.Menus;
import me.refracdevelopment.simplegems.manager.data.DataType;
import me.refracdevelopment.simplegems.manager.data.PlayerMapper;
import me.refracdevelopment.simplegems.manager.data.mongo.MongoManager;
import me.refracdevelopment.simplegems.manager.data.sql.MySQLManager;
import me.refracdevelopment.simplegems.manager.data.sql.SQLiteManager;
import me.refracdevelopment.simplegems.manager.leaderboards.LeaderboardManager;
import me.refracdevelopment.simplegems.menu.GemShop;
import me.refracdevelopment.simplegems.utilities.DownloadUtil;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.PAPIExpansion;
import me.refracdevelopment.simplegems.utilities.command.CommandList;
import me.refracdevelopment.simplegems.utilities.command.SubCommand;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public final class SimpleGems extends JavaPlugin {

    @Getter
    private static SimpleGems instance;

    // Managers
    private DataType dataType;
    private MongoManager mongoManager;
    private MySQLManager mySQLManager;
    private SQLiteManager sqLiteManager;
    private PlayerMapper playerMapper;
    private ProfileManager profileManager;
    private ActionManager actionManager;
    private LeaderboardManager leaderboardManager;
    private MenuManager menuManager;
    private CommandManager commandManager;
    
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
    private SimpleGemsAPI gemsAPI;
    private FoliaLib foliaLib;
    private final List<SubCommand> subCommands = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        long startTiming = System.currentTimeMillis();
        PluginManager pluginManager = getServer().getPluginManager();

        foliaLib = new FoliaLib(this);

        DownloadUtil.downloadAndEnable();

        loadFiles();

        new Metrics(this, 13117);

        // Check if the server is on 1.7
        if (ReflectionUtils.MINOR_NUMBER <= 7) {
            Color.log("&c" + getDescription().getName() + " 1.7 is in legacy mode, please update to 1.8+");
            pluginManager.disablePlugin(this);
            return;
        }

        // Check if the server is on Folia
        if (getFoliaLib().isFolia()) {
            Color.log("&cSupport for Folia has not been tested and is only for experimental purposes.");
        }

        // Make sure the server has PlaceholderAPI
        if (!pluginManager.isPluginEnabled("PlaceholderAPI")) {
            Color.log("&cPlease install PlaceholderAPI onto your server to use this plugin.");
            pluginManager.disablePlugin(this);
            return;
        }

        // Make sure the server has NBTAPI
        if (!pluginManager.isPluginEnabled("NBTAPI")) {
            Color.log("&cPlease install NBTAPI onto your server to use this plugin.");
            pluginManager.disablePlugin(this);
            return;
        }

        if (pluginManager.isPluginEnabled("Skulls")) {
            Color.log("&aSkulls Detected!");
        }

        if (pluginManager.isPluginEnabled("HeadDatabase")) {
            Color.log("&aHeadDatabase Detected!");
        }

        loadManagers();
        loadCommands();
        loadListeners();

        new PAPIExpansion().register();

        Color.log("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");
        Color.log("&e" + getDescription().getName() + " has been enabled. (took " + (System.currentTimeMillis() - startTiming) + "ms)");
        Color.log(" &f[*] &6Version&f: &b" + getDescription().getVersion());
        Color.log(" &f[*] &6Name&f: &b" + getDescription().getName());
        Color.log(" &f[*] &6Author&f: &b" + getDescription().getAuthors().get(0));
        Color.log("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");

        updateCheck(getServer().getConsoleSender(), true);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        switch (getDataType()) {
            case MYSQL:
                getMySQLManager().shutdown();
                break;
            case SQLITE:
                getSqLiteManager().shutdown();
                break;
        }
    }

    public void loadFiles() {
        // Files
        configFile = new ConfigFile("config.yml");
        menusFile = new ConfigFile("menus.yml");
        commandsFile = new ConfigFile("commands/gems.yml");
        localeFile = new ConfigFile("locale/" + getConfigFile().getString("locale") + ".yml");

        // Cache
        settings = new Config();
        menus = new Menus();
        commands = new Commands();
    }

    public void reloadFiles() {
        // Files
        getConfigFile().reload();
        getMenusFile().reload();
        getCommandsFile().reload();
        getLocaleFile().reload();

        // Cache
        getSettings().loadConfig();
        getMenus().loadConfig();
        getCommands().loadConfig();
    }

    private void loadManagers() {
        switch (getSettings().DATA_TYPE.toUpperCase()) {
            case "MONGODB":
            case "MONGO":
                dataType = DataType.MONGO;
                mongoManager = new MongoManager();
                getMongoManager().connect();
                Color.log("&aEnabled MongoDB support.");
                break;
            case "MARIADB":
            case "MYSQL":
                dataType = DataType.MYSQL;
                mySQLManager = new MySQLManager();
                getMySQLManager().connect();
                getMySQLManager().createT();
                Color.log("&aEnabled MySQL support!");
                break;
            case "SQLITE":
                dataType = DataType.SQLITE;
                sqLiteManager = new SQLiteManager();
                getSqLiteManager().connect(getDataFolder().getAbsolutePath() + File.separator + "gems.db");
                getSqLiteManager().createT();
                Color.log("&aEnabled SQLite support!");
                break;
            default:
                dataType = DataType.FLAT_FILE;
                playerMapper = new PlayerMapper(getDataFolder().getAbsolutePath() + File.separator + "playerdata");
                Color.log("&aEnabled Flat File support!");
                break;
        }

        profileManager = new ProfileManager();
        gemsAPI = new SimpleGemsAPI();
        actionManager = new ActionManager(this);
        leaderboardManager = new LeaderboardManager();
        menuManager = new MenuManager();
        commandManager = new CommandManager();
        Color.log("&aLoaded managers.");
    }

    private void loadCommands() {
        try {
            getCommandManager().createCoreCommand(this, getCommands().GEMS_COMMAND_NAME,
                    getLocaleFile().getString("command-help-description"),
                    "/" + getCommands().GEMS_COMMAND_NAME, new CommandList() {
                        @Override
                        public void displayCommandList(CommandSender commandSender, List<SubCommand> list) {
                            getSettings().GEMS_BALANCE.forEach(message -> {
                                Color.sendCustomMessage(commandSender, message);
                            });
                        }
                    }, getCommands().GEMS_COMMAND_ALIASES,
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
                    ResetCommand.class
            );

            getSubCommands().addAll(Arrays.asList(
                    new HelpCommand(),
                    new BalanceCommand(),
                    new TopCommand(),
                    new ShopCommand(),
                    new WithdrawCommand(),
                    new PayCommand(),
                    new GiveCommand(),
                    new TakeCommand(),
                    new SetCommand(),
                    new ReloadCommand(),
                    new VersionCommand(),
                    new UpdateCommand(),
                    new ResetCommand()
            ));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Color.log("&aFailed to load commands.");
            e.printStackTrace();
            return;
        }
        Color.log("&aLoaded commands.");
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        gemShop = new GemShop();
        Color.log("&aLoaded listeners.");
    }

    public void updateCheck(CommandSender sender, boolean console) {
        try {
            String urlString = "https://refracdev-updatecheck.refracdev.workers.dev/";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            StringBuffer response = new StringBuffer();
            while ((input = reader.readLine()) != null) {
                response.append(input);
            }
            reader.close();
            JsonObject object = new JsonParser().parse(response.toString()).getAsJsonObject();

            if (object.has("plugins")) {
                JsonObject plugins = object.get("plugins").getAsJsonObject();
                JsonObject info = plugins.get(this.getDescription().getName()).getAsJsonObject();
                String version = info.get("version").getAsString();
                if (version.equals(getDescription().getVersion())) {
                    if (console) {
                        sender.sendMessage(Color.translate("&a" + getDescription().getName() + " is on the latest version."));
                    }
                } else {
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cYour " + getDescription().getName() + " version is out of date!"));
                    sender.sendMessage(Color.translate("&cWe recommend updating ASAP!"));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cYour Version: &e" + getDescription().getVersion()));
                    sender.sendMessage(Color.translate("&aNewest Version: &e" + version));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    return;
                }
            } else {
                sender.sendMessage(Color.translate("&cWrong response from update API, contact plugin developer!"));
            }
        } catch (Exception ex) {
            sender.sendMessage(Color.translate("&cFailed to get updater check. (" + ex.getMessage() + ")"));
        }
    }
}