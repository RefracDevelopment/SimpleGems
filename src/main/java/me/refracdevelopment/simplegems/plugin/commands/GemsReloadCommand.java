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
package me.refracdevelopment.simplegems.plugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.menu.GemShopItem;
import me.refracdevelopment.simplegems.plugin.utilities.Methods;
import me.refracdevelopment.simplegems.plugin.utilities.Permissions;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Color;
import me.refracdevelopment.simplegems.plugin.utilities.files.Files;
import me.refracdevelopment.simplegems.plugin.utilities.files.Menus;
import me.refracdevelopment.simplegems.plugin.utilities.files.Messages;
import org.bukkit.command.CommandSender;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
@CommandAlias("gemsreload")
@Description("Reloads the config files")
@CommandPermission(Permissions.GEMS_ADMIN)
@Conditions("noconsole")
public class GemsReloadCommand extends BaseCommand {

    @Dependency
    private SimpleGems plugin;


    @Default
    public void onDefault(CommandSender sender, String[] args) {
        Files.reloadFiles(plugin);
        plugin.getServer().getScheduler().cancelTasks(plugin);
        Methods.saveTask();
        plugin.getGemShop().getItems().clear();
        for (String item : Menus.GEM_SHOP_ITEMS.getKeys(false))
            plugin.getGemShop().getItems().put(item, new GemShopItem(item));
        Color.sendMessage(sender, Messages.RELOAD, true, true);
    }
}