package me.refracdevelopment.simplegems;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import dev.rosewood.rosegarden.utils.NMSUtil;
import lombok.Getter;
import me.gabytm.util.actions.ActionManager;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.listeners.MenuListener;
import me.refracdevelopment.simplegems.listeners.PlayerListener;
import me.refracdevelopment.simplegems.manager.CommandManager;
import me.refracdevelopment.simplegems.manager.MenuManager;
import me.refracdevelopment.simplegems.manager.configuration.ConfigFile;
import me.refracdevelopment.simplegems.manager.configuration.ConfigurationManager;
import me.refracdevelopment.simplegems.manager.configuration.LocaleManager;
import me.refracdevelopment.simplegems.manager.configuration.cache.Config;
import me.refracdevelopment.simplegems.manager.configuration.cache.Menus;
import me.refracdevelopment.simplegems.manager.data.DataType;
import me.refracdevelopment.simplegems.manager.data.mongo.MongoManager;
import me.refracdevelopment.simplegems.manager.data.sql.MySQLManager;
import me.refracdevelopment.simplegems.manager.data.PlayerMapper;
import me.refracdevelopment.simplegems.manager.data.sql.SQLiteManager;
import me.refracdevelopment.simplegems.manager.leaderboards.LeaderboardManager;
import me.refracdevelopment.simplegems.menu.GemShop;
import me.refracdevelopment.simplegems.player.data.ProfileManager;
import me.refracdevelopment.simplegems.utilities.DownloadUtil;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.PAPIExpansion;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Getter
public final class SimpleGems extends RosePlugin {

    @Getter
    private static SimpleGems instance;

    private DataType dataType;
    private MongoManager mongoManager;
    private MySQLManager mySQLManager;
    private SQLiteManager sqLiteManager;
    private ProfileManager profileManager;
    private SimpleGemsAPI gemsAPI;
    private ActionManager actionManager;
    private LeaderboardManager leaderboardManager;
    private MenuManager menuManager;

    private GemShop gemShop;

    private ConfigFile menusFile;
    private PlayerMapper playerMapper;

    public SimpleGems() {
        super(-1, 13117, ConfigurationManager.class, null, LocaleManager.class, CommandManager.class);
        instance = this;
    }

    @Override
    protected void enable() {
        // Plugin startup logic
        long startTiming = System.currentTimeMillis();
        PluginManager pluginManager = this.getServer().getPluginManager();

        DownloadUtil.downloadAndEnable();

        // Check if the server is on 1.7
        if (NMSUtil.getVersionNumber() <= 7) {
            Color.log("&cSimpleGems 1.7 is in legacy mode, please update to 1.8+");
            pluginManager.disablePlugin(this);
            return;
        }

        // Make sure the server has PlaceholderAPI
        if (pluginManager.getPlugin("PlaceholderAPI") == null) {
            Color.log("&cPlease install PlaceholderAPI onto your server to use this plugin.");
            pluginManager.disablePlugin(this);
            return;
        }

        // Make sure the server has NBTAPI
        if (pluginManager.getPlugin("NBTAPI") == null) {
            Color.log("&cPlease install NBTAPI onto your server to use this plugin.");
            pluginManager.disablePlugin(this);
            return;
        }

        if (pluginManager.getPlugin("Skulls") != null) {
            Color.log("&eSkulls Detected!");
        }

        if (pluginManager.getPlugin("HeadDatabase") != null) {
            Color.log("&eHeadDatabase Detected!");
        }

        loadFiles();

        this.loadManagers();
        Color.log("&aLoaded commands.");
        this.loadListeners();

        new PAPIExpansion().register();

        Color.log("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");
        Color.log("&e" + this.getDescription().getName() + " has been enabled. (" + (System.currentTimeMillis() - startTiming) + "ms)");
        Color.log(" &f[*] &6Version&f: &b" + this.getDescription().getVersion());
        Color.log(" &f[*] &6Name&f: &b" + this.getDescription().getName());
        Color.log(" &f[*] &6Author&f: &b" + this.getDescription().getAuthors().get(0));
        Color.log("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");

        updateCheck(this.getServer().getConsoleSender(), true);
    }

    @Override
    protected void disable() {
        // Plugin shutdown logic
        if (dataType == DataType.MYSQL) {
            mySQLManager.shutdown();
        } else if (dataType == DataType.SQLITE) {
            try {
                sqLiteManager.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        this.getServer().getScheduler().cancelTasks(this);
    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return Collections.emptyList();
    }

    public void loadFiles() {
        menusFile = new ConfigFile(this, "menus.yml");
        Config.loadConfig();
        Menus.loadMenus();
    }

    public void reloadFiles() {
        menusFile.load();
        Config.loadConfig();
        Menus.loadMenus();
    }

    private void loadManagers() {
        switch (Config.DATA_TYPE.toUpperCase()) {
            case "MONGODB":
            case "MONGO":
                dataType = DataType.MONGO;
                mongoManager = new MongoManager(this);
                getMongoManager().connect();
                Color.log("&aEnabled MongoDB support.");
                break;
            case "MYSQL":
                dataType = DataType.MYSQL;
                mySQLManager = new MySQLManager(this);
                getMySQLManager().connect();
                getMySQLManager().createT();
                Color.log("&aEnabled MySQL support!");
                break;
            case "SQLITE":
                dataType = DataType.SQLITE;
                sqLiteManager = new SQLiteManager(this);
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
        leaderboardManager = new LeaderboardManager(this);
        menuManager = new MenuManager();
        Color.log("&aLoaded managers.");
    }

    private void loadListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
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
                        sender.sendMessage(Color.translate("&a" + this.getDescription().getName() + " is on the latest version."));
                    }
                } else {
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cYour " + this.getDescription().getName() + " version is out of date!"));
                    sender.sendMessage(Color.translate("&cWe recommend updating ASAP!"));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cYour Version: &e" + this.getDescription().getVersion()));
                    sender.sendMessage(Color.translate("&aNewest Version: &e" + version));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    return;
                }
                return;
            } else {
                sender.sendMessage(Color.translate("&cWrong response from update API, contact plugin developer!"));
                return;
            }
        } catch (
                Exception ex) {
            sender.sendMessage(Color.translate("&cFailed to get updater check. (" + ex.getMessage() + ")"));
            return;
        }
    }
}