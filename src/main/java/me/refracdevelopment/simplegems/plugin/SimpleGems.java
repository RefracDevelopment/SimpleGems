/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 RefracDevelopment
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.refracdevelopment.simplegems.plugin;

import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simplegems.plugin.api.GemsAPI;
import me.refracdevelopment.simplegems.plugin.listeners.DepositListener;
import me.refracdevelopment.simplegems.plugin.listeners.JoinListener;
import me.refracdevelopment.simplegems.plugin.manager.CommandManager;
import me.refracdevelopment.simplegems.plugin.manager.ProfileManager;
import me.refracdevelopment.simplegems.plugin.manager.database.DataType;
import me.refracdevelopment.simplegems.plugin.manager.database.SQLManager;
import me.refracdevelopment.simplegems.plugin.menu.GemShop;
import me.refracdevelopment.simplegems.plugin.utilities.Glow;
import me.refracdevelopment.simplegems.plugin.utilities.Methods;
import me.refracdevelopment.simplegems.plugin.utilities.Settings;
import me.refracdevelopment.simplegems.plugin.utilities.VersionCheck;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Color;
import me.refracdevelopment.simplegems.plugin.utilities.chat.PlaceHolderExpansion;
import me.refracdevelopment.simplegems.plugin.utilities.files.Config;
import me.refracdevelopment.simplegems.plugin.utilities.files.Files;
import org.bstats.bukkit.Metrics;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-1
 */
@Getter
@Setter
public final class SimpleGems extends JavaPlugin {

    @Getter
    private static SimpleGems instance;

    private DataType dataType;
    private SQLManager sqlManager;
    private ProfileManager profileManager;
    private Glow glow;
    private GemShop gemShop;
    private GemsAPI gemsAPI;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        Files.loadFiles(this);

        switch (Config.DATA_TYPE.toUpperCase()) {
            case "MYSQL":
                this.dataType = DataType.MYSQL;
                break;
            case "YAML":
                this.dataType = DataType.YAML;
                break;
            default:
                this.dataType = DataType.YAML;
                break;
        }

        if (this.dataType == DataType.MYSQL) {
            this.sqlManager = new SQLManager(this);
        }

        this.profileManager = new ProfileManager(this);
        this.gemsAPI = new GemsAPI();

        CommandManager.registerAll();
        this.loadListeners();

        if(VersionCheck.isOnePointThirteenPlus()) {
            this.glow = new Glow(NamespacedKey.minecraft("glow"));
            this.glow.register();
            Color.log("Enabled glow for Spigot 1.13+");
        }

        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHolderExpansion().register();
            Color.log("&aPlaceholder API expansion enabled.");
        }

        new Metrics(this, 13117);

        // Save all online players every x seconds
        Methods.saveTask();

        Color.log("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");
        Color.log("&e" + Settings.getName + " has been enabled.");
        Color.log(" &f[*] &6Version&f: &b" + Settings.getVersion);
        Color.log(" &f[*] &6Name&f: &b" + Settings.getName);
        Color.log(" &f[*] &6Author&f: &b" + Settings.getDeveloper);
        Color.log("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");
    }

    @Override
    public void onDisable() {
       this.getServer().getOnlinePlayers().forEach(player -> {
           this.profileManager.getProfile(player.getUniqueId()).getData().save();
       });
    }

    private void loadListeners() {
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new DepositListener(this), this);
        this.gemShop = new GemShop(this);
    }
}