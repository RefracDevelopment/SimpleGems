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

import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.ConditionFailedException;
import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simplegems.plugin.api.GemsAPI;
import me.refracdevelopment.simplegems.plugin.commands.*;
import me.refracdevelopment.simplegems.plugin.listeners.DepositListener;
import me.refracdevelopment.simplegems.plugin.listeners.JoinListener;
import me.refracdevelopment.simplegems.plugin.manager.ProfileManager;
import me.refracdevelopment.simplegems.plugin.manager.database.DataType;
import me.refracdevelopment.simplegems.plugin.manager.database.SQLManager;
import me.refracdevelopment.simplegems.plugin.menu.GemShop;
import me.refracdevelopment.simplegems.plugin.utilities.Logger;
import me.refracdevelopment.simplegems.plugin.utilities.Methods;
import me.refracdevelopment.simplegems.plugin.utilities.Settings;
import me.refracdevelopment.simplegems.plugin.utilities.chat.PlaceHolderExpansion;
import me.refracdevelopment.simplegems.plugin.utilities.files.Config;
import me.refracdevelopment.simplegems.plugin.utilities.files.Files;
import org.bstats.bukkit.Metrics;
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

    private DataType dataType;
    private SQLManager sqlManager;
    private ProfileManager profileManager;
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
            (this.sqlManager = new SQLManager()).connect();
        }

        if (this.sqlManager != null && this.sqlManager.getConnection() == null && this.dataType == DataType.MYSQL) {
            Logger.ERROR.out("Disabling SimpleGems due to issues with connection for MySQL servers, check your SQL information.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.profileManager = new ProfileManager(this);
        this.gemsAPI = new GemsAPI();

        this.loadCommands();
        this.loadListeners();

        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
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
        if (this.dataType != DataType.YAML) {
            Logger.SUCCESS.out("SimpleGems is using &2MySQL &aas a database.");
        }
        Logger.NONE.out("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");
    }

    @Override
    public void onDisable() {
        instance = null;
        for (Player player : this.getServer().getOnlinePlayers())
            this.profileManager.getProfile(player.getUniqueId()).getData().save();
        if (this.dataType == DataType.MYSQL) {
            this.sqlManager.close();
        }
        this.getServer().getScheduler().cancelTasks(this);
    }

    private void loadCommands() {
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.getCommandConditions().addCondition("noconsole", (context) -> {
            BukkitCommandIssuer issuer = context.getIssuer();
            if (!issuer.isPlayer()) {
                throw new ConditionFailedException("Only players can execute this command.");
            }
        });
        manager.getCommandConditions().addCondition("noplayer", (context) -> {
            BukkitCommandIssuer issuer = context.getIssuer();
            if (issuer.isPlayer()) {
                throw new ConditionFailedException("Only console can execute this command.");
            }
        });
        manager.registerCommand(new GemsCommand());
        manager.registerCommand(new ConsoleGemsCommand());
        manager.registerCommand(new GemShopCommand());
        manager.registerCommand(new GemsReloadCommand());
        manager.registerCommand(new GemsTopCommand());
    }

    private void loadListeners() {
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new DepositListener(this), this);
        this.gemShop = new GemShop(this);
    }
}