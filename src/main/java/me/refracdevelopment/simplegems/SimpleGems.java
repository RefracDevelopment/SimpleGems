package me.refracdevelopment.simplegems;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import dev.rosewood.rosegarden.utils.NMSUtil;
import lombok.Getter;
import me.gabytm.util.actions.ActionManager;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.data.ProfileManager;
import me.refracdevelopment.simplegems.database.DataType;
import me.refracdevelopment.simplegems.database.mongo.MongoManager;
import me.refracdevelopment.simplegems.database.mysql.MySQLManager;
import me.refracdevelopment.simplegems.listeners.PlayerListener;
import me.refracdevelopment.simplegems.manager.CommandManager;
import me.refracdevelopment.simplegems.manager.ConfigurationManager;
import me.refracdevelopment.simplegems.manager.LeaderboardManager;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.menu.GemShop;
import me.refracdevelopment.simplegems.utilities.DownloadUtil;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.PAPIExpansion;
import me.refracdevelopment.simplegems.utilities.config.Config;
import me.refracdevelopment.simplegems.utilities.config.ConfigFile;
import me.refracdevelopment.simplegems.utilities.config.Menus;
import me.refracdevelopment.simplegems.utilities.config.PlayerMapper;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Getter
public final class SimpleGems extends RosePlugin {

    @Getter
    private static SimpleGems instance;

    private ProfileManager profileManager;
    private SimpleGemsAPI gemsAPI;
    private ActionManager actionManager;
    private LeaderboardManager leaderboardManager;

    private GemShop gemShop;

    private ConfigFile menusFile;
    private PlayerMapper playerMapper;
    private DataType dataType;
    private MongoManager mongoManager;
    private MySQLManager mySQLManager;

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

        // Make sure the server has PlaceholderAPI
        if (!pluginManager.isPluginEnabled("PlaceholderAPI")) {
            Color.log("&cPlease install PlaceholderAPI onto your server to use this plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Make sure the server is on MC 1.16
        if (NMSUtil.getVersionNumber() < 16) {
            Color.log("&cThis plugin only supports 1.16+ Minecraft.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (getServer().getPluginManager().getPlugin("Skulls") != null) {
            Color.log("&eSkulls Detected!");
        }

        if (getServer().getPluginManager().getPlugin("HeadDatabase") != null) {
            Color.log("&eHeadDatabase Detected!");
        }

        Config.loadConfig();
        menusFile = new ConfigFile(this, "menus.yml");
        Menus.loadMenus(this);

        this.loadManagers();
        Color.log("&eLoaded commands.");
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
        this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            profileManager.getProfiles().values().forEach(profile -> profile.getData().save());
        });
        this.getServer().getScheduler().cancelTasks(this);
    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of();
    }

    private void loadManagers() {
        switch (Config.DATA_TYPE.toUpperCase()) {
            case "MONGODB":
            case "MONGO":
                dataType = DataType.MONGO;
                break;
            case "MYSQL":
                dataType = DataType.MYSQL;
                break;
            case "YAML":
            case "FLAT_FILE":
                dataType = DataType.FLAT_FILE;
                break;
            default:
                dataType = DataType.FLAT_FILE;
                break;
        }

        if (dataType == DataType.MONGO) {
            mongoManager = new MongoManager(this);
            getMongoManager().connect();
            Color.log("&eEnabled MongoDB support.");
        } else if (dataType == DataType.MYSQL) {
            mySQLManager = new MySQLManager(this);
            getMySQLManager().connect();
            getMySQLManager().createT();
            Color.log("&eEnabled MySQL support!");
        } else if (dataType == DataType.FLAT_FILE) {
            playerMapper = new PlayerMapper(getDataFolder().getAbsolutePath() + File.separator + "playerdata");
            Color.log("&eEnabled Flat File support!");
        }

        profileManager = new ProfileManager();
        gemsAPI = new SimpleGemsAPI();
        actionManager = new ActionManager(this);
        leaderboardManager = new LeaderboardManager(this);
        Color.log("&eLoaded Leaderboards!");
        Color.log("&eLoaded managers.");
    }

    private void loadListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        gemShop = new GemShop();
        Color.log("&eLoaded listeners.");
    }

    public void updateCheck(CommandSender sender, boolean console) {
        try {
            String urlString = "https://updatecheck.refracdev.ml/";
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