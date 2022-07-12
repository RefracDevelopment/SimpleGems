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
import me.refracdevelopment.simplegems.plugin.commands.*;
import me.refracdevelopment.simplegems.plugin.listeners.DepositListener;
import me.refracdevelopment.simplegems.plugin.listeners.JoinListener;
import me.refracdevelopment.simplegems.plugin.manager.ProfileManager;
import me.refracdevelopment.simplegems.plugin.manager.database.SQLManager;
import me.refracdevelopment.simplegems.plugin.menu.GemShop;
import me.refracdevelopment.simplegems.plugin.utilities.Logger;
import me.refracdevelopment.simplegems.plugin.utilities.Methods;
import me.refracdevelopment.simplegems.plugin.utilities.Settings;
import me.refracdevelopment.simplegems.plugin.utilities.chat.PlaceHolderExpansion;
import me.refracdevelopment.simplegems.plugin.utilities.files.Files;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

    private SQLManager sqlManager;
    private ProfileManager profileManager;
    private GemShop gemShop;
    private GemsAPI gemsAPI;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        Files.loadFiles(this);

        (this.sqlManager = new SQLManager()).connect();

        if (this.sqlManager != null && this.sqlManager.getConnection() == null) {
            Logger.ERROR.out("Disabling SimpleGems due to issues with connection for MySQL servers, check your SQL information.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.profileManager = new ProfileManager(this);
        this.gemsAPI = new GemsAPI();

        this.loadCommands();
        this.loadListeners();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHolderExpansion().register();
            Logger.INFO.out("Placeholder API expansion successfully registered.");
        }

        // Save all online players every x seconds
        Methods.saveTask();

        new Metrics(this, 13117);

        Logger.NONE.out("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");
        Logger.NONE.out("&e" + Settings.getName + " has been enabled.");
        Logger.NONE.out(" &f[*] &6Version&f: &b" + Settings.getVersion);
        Logger.NONE.out(" &f[*] &6Name&f: &b" + Settings.getName);
        Logger.NONE.out(" &f[*] &6Author&f: &b" + Settings.getDeveloper);
        Logger.SUCCESS.out("SimpleGems is using &2MySQL &aas a database.");
        Logger.NONE.out("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");
    }

    @Override
    public void onDisable() {
        instance = null;
        for (Player player : Bukkit.getOnlinePlayers())
            this.profileManager.getProfile(player.getUniqueId()).getData().save();
        this.sqlManager.close();
        getServer().getScheduler().cancelTasks(this);
    }

    private void loadCommands() {
        getCommand("gems").setExecutor(new GemsCommand(this));
        getCommand("gems").setTabCompleter(new GemsCommand(this));
        getCommand("consolegems").setExecutor(new ConsoleGemsCommand(this));
        getCommand("gemsreload").setExecutor(new GemsReloadCommand(this));
        getCommand("gemshop").setExecutor(new GemShopCommand(this));
        getCommand("gemstop").setExecutor(new GemsTopCommand(this));
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new DepositListener(this), this);
        this.gemShop = new GemShop(this);
    }
}