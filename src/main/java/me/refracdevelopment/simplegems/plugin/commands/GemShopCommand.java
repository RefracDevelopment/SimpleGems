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

import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.utilities.Manager;
import me.refracdevelopment.simplegems.plugin.utilities.Permissions;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Color;
import me.refracdevelopment.simplegems.plugin.utilities.files.Menus;
import me.refracdevelopment.simplegems.plugin.utilities.files.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class GemShopCommand extends Manager implements CommandExecutor {

    public GemShopCommand(SimpleGems plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            Color.sendMessage(sender, "&cYou must be a player to execute this command.", true, true);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(Permissions.GEMS_SHOP)) {
            Color.sendMessage(player, Messages.NO_PERMISSION, true, true);
            return true;
        }

        if (Menus.GEM_SHOP_ENABLED) {
            plugin.getGemShop().getGemShop().openInventory(player);
        } else Color.sendMessage(player, Messages.SHOP_DISABLED, true, true);
        return true;
    }
}