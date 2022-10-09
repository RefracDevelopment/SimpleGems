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
package me.refracdevelopment.simplegems.plugin.command.commands;

import com.google.common.base.Joiner;
import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.command.Command;
import me.refracdevelopment.simplegems.plugin.manager.Profile;
import me.refracdevelopment.simplegems.plugin.utilities.Methods;
import me.refracdevelopment.simplegems.plugin.utilities.Permissions;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Color;
import me.refracdevelopment.simplegems.plugin.utilities.files.Config;
import me.refracdevelopment.simplegems.plugin.utilities.files.Messages;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class ConsoleGemsCommand extends Command {

    private final SimpleGems plugin;

    public ConsoleGemsCommand() {
        super(Config.CONSOLE_GEMS_COMMAND, Config.CONSOLE_GEMS_ALIASES);
        this.plugin = SimpleGems.getInstance();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!Config.CONSOLE_GEMS_COMMAND_ENABLED) return true;
        if (sender instanceof Player) return true;
        if (!sender.hasPermission(Permissions.GEMS_ADMIN)) {
            Color.sendMessage(sender, Messages.NO_PERMISSION, true, true);
            return true;
        }

        if (args.length == 0) {
            for (String message : Messages.GEMS_BALANCE)
                Color.sendMessage(sender, message, true, true);
        } else if (args.length >= 1) {
            String message = Joiner.on(" ").join(args);

            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")) {
                if (plugin.getServer().getPlayer(args[1]) != null) {
                    Player target = plugin.getServer().getPlayer(args[1]);
                    Profile targetProfile = plugin.getProfileManager().getProfile(target.getUniqueId());

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(sender, Messages.INVALID_AMOUNT.replace("%amount%", args[1]), true, true);
                        return true;
                    }

                    targetProfile.getData().getGems().incrementStat(amount);

                    Color.sendMessage(sender, Messages.GEMS_GIVEN.replace("%gems%", Methods.format(amount)).replace("%sender%", target.getName()), true, true);
                    if (message.contains("-s")) return true;
                    Color.sendMessage(target, Messages.GEMS_GAINED.replace("%gems%", Methods.format(amount)), true, true);
                } else if (plugin.getServer().getOfflinePlayer(args[1]) != null && plugin.getServer().getOfflinePlayer(args[1]).hasPlayedBefore()) {
                    OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[1]);

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(sender, Messages.INVALID_AMOUNT.replace("%amount%", args[1]), true, true);
                        return true;
                    }

                    Methods.giveOfflineGems(target, amount);

                    Color.sendMessage(sender, Messages.GEMS_GIVEN.replace("%gems%", Methods.format(amount)).replace("%sender%", target.getName()), true, true);
                } else Color.sendMessage(sender, Messages.INVALID_PLAYER.replace("%sender%", args[1]), true, true);
            } else if (args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")) {
                if (plugin.getServer().getPlayer(args[1]) != null) {
                    Player target = plugin.getServer().getPlayer(args[1]);
                    Profile targetProfile = plugin.getProfileManager().getProfile(target.getUniqueId());

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(sender, Messages.INVALID_AMOUNT.replace("%amount%", args[1]), true, true);
                        return true;
                    }

                    if (!targetProfile.getData().getGems().hasStat(amount)) {
                        Color.sendMessage(sender, Messages.INVALID_GEMS.replace("%sender%", target.getName()), true, true);
                        return true;
                    }

                    targetProfile.getData().getGems().decrementStat(amount);

                    Color.sendMessage(sender, Messages.GEMS_TAKEN.replace("%gems%", Methods.format(amount)).replace("%sender%", target.getName()), true, true);
                    if (message.contains("-s")) return true;
                    Color.sendMessage(target, Messages.GEMS_LOST.replace("%gems%", Methods.format(amount)), true, true);
                } else if (plugin.getServer().getOfflinePlayer(args[1]) != null && plugin.getServer().getOfflinePlayer(args[1]).hasPlayedBefore()) {
                    OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[1]);

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(sender, Messages.INVALID_AMOUNT.replace("%amount%", args[1]), true, true);
                        return true;
                    }

                    if (!Methods.hasOfflineGems(target, amount)) {
                        Color.sendMessage(sender, Messages.INVALID_GEMS.replace("%sender%", target.getName()), true, true);
                        return true;
                    }

                    Methods.takeOfflineGems(target, amount);

                    Color.sendMessage(sender, Messages.GEMS_TAKEN.replace("%gems%", Methods.format(amount)).replace("%sender%", target.getName()), true, true);
                } else Color.sendMessage(sender, Messages.INVALID_PLAYER.replace("%sender%", args[1]), true, true);
            } else if (args[0].equalsIgnoreCase("set")) {
                if (plugin.getServer().getPlayer(args[1]) != null) {
                    Player target = plugin.getServer().getPlayer(args[1]);
                    Profile targetProfile = plugin.getProfileManager().getProfile(target.getUniqueId());

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(sender, Messages.INVALID_AMOUNT.replace("%amount%", args[1]), true, true);
                        return true;
                    }

                    targetProfile.getData().getGems().setStat(amount);

                    Color.sendMessage(sender, Messages.GEMS_SET.replace("%gems%", Methods.format(amount)).replace("%sender%", target.getName()), true, true);
                    if (message.contains("-s")) return true;
                    Color.sendMessage(target, Messages.GEMS_SETTED.replace("%gems%", Methods.format(amount)), true, true);
                } else if (plugin.getServer().getOfflinePlayer(args[1]) != null && plugin.getServer().getOfflinePlayer(args[1]).hasPlayedBefore()) {
                    OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[1]);

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(sender, Messages.INVALID_AMOUNT, true, true);
                        return true;
                    }

                    Methods.setOfflineGems(target, amount);

                    Color.sendMessage(sender, Messages.GEMS_SET.replace("%gems%", Methods.format(amount)).replace("%sender%", target.getName()), true, true);
                } else Color.sendMessage(sender, Messages.INVALID_PLAYER.replace("%sender%", args[1]), true, true);
            }
        }
        return true;
    }

    @Override
    public int compareTo(@NotNull Command o) {
        return 0;
    }
}