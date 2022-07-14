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

import com.google.common.base.Joiner;
import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.utilities.Manager;
import me.refracdevelopment.simplegems.plugin.utilities.Methods;
import me.refracdevelopment.simplegems.plugin.utilities.Permissions;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Color;
import me.refracdevelopment.simplegems.plugin.utilities.files.Menus;
import me.refracdevelopment.simplegems.plugin.utilities.files.Messages;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class GemsCommand extends Manager implements CommandExecutor, TabCompleter {

    public GemsCommand(SimpleGems plugin) {
        super(plugin);
    }

    private static final List<String> COMMANDS = Arrays.asList("shop", "balance", "withdraw", "pay", "give", "add", "take", "remove", "set");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            Color.sendMessage(sender, "&cYou must be a player to execute this command.", true, true);
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            for (String message : Messages.HELP_PAGE)
                Color.sendMessage(player, message, true, true);
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("shop")) {
                if (!player.hasPermission(Permissions.GEMS_SHOP)) {
                    Color.sendMessage(player, Messages.NO_PERMISSION, true, true);
                    return true;
                }

                if (Menus.GEM_SHOP_ENABLED) {
                    plugin.getGemShop().getGemShop().openInventory(player);
                } else Color.sendMessage(player, Messages.SHOP_DISABLED, true, true);
                return true;
            } else if (args[0].equalsIgnoreCase("balance")) {
                if (!player.hasPermission(Permissions.GEMS_BALANCE)) {
                    Color.sendMessage(player, Messages.NO_PERMISSION, true, true);
                    return true;
                }

                double amount = Methods.getGems(player);

                Color.sendMessage(player, Messages.GEMS_BALANCE.replace("%gems%", Methods.format(amount)).replace("%player%", player.getName())
                        .replace("%gems_decimal%", Methods.formatDec(amount)), true, true);
            }
        } else if (args.length >= 1) {
            String message = Joiner.on(" ").join(args);

            if (args[0].equalsIgnoreCase("balance")) {
                if (!player.hasPermission(Permissions.GEMS_ADMIN)) {
                    Color.sendMessage(player, Messages.NO_PERMISSION, true, true);
                    return true;
                }

                if (plugin.getServer().getPlayer(args[1]) != null) {
                    Player target = plugin.getServer().getPlayer(args[1]);

                    double amount = Methods.getGems(target);

                    Color.sendMessage(player, Messages.GEMS_BALANCE.replace("%gems%", Methods.format(amount)).replace("%player%", target.getName())
                            .replace("%gems_decimal%", Methods.formatDec(amount)), true, true);
                } else if (plugin.getServer().getOfflinePlayer(args[1]) != null && plugin.getServer().getOfflinePlayer(args[1]).hasPlayedBefore()) {
                    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                        OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[1]);

                        double amount = Methods.getOfflineGems(target);

                        Color.sendMessage(player, Messages.GEMS_BALANCE.replace("%gems%", Methods.format(amount)).replace("%player%", target.getName())
                                .replace("%gems_decimal%", Methods.formatDec(amount)), true, true);
                    });
                } else Color.sendMessage(player, Messages.INVALID_PLAYER, true, true);
                return true;
            } else if (args[0].equalsIgnoreCase("withdraw")) {
                if (!player.hasPermission(Permissions.GEMS_WITHDRAW)) {
                    Color.sendMessage(player, Messages.NO_PERMISSION, true, false);
                    return true;
                }

                int amount;

                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    Color.sendMessage(player, Messages.INVALID_AMOUNT.replace("%amount%", args[2]), true, true);
                    return true;
                }

                Methods.withdrawGems(player, amount);
            } else if (args[0].equalsIgnoreCase("pay")) {
                if (!player.hasPermission(Permissions.GEMS_PAY)) {
                    Color.sendMessage(player, Messages.NO_PERMISSION, true, true);
                    return true;
                }

                if (plugin.getServer().getPlayer(args[1]) != null) {
                    Player target = plugin.getServer().getPlayer(args[1]);

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(player, Messages.INVALID_AMOUNT.replace("%amount%", args[2]), true, true);
                        return true;
                    }

                    boolean silent = message.contains("-s") && player.hasPermission(Permissions.GEMS_ADMIN);

                    Methods.payGems(player, target, amount, silent);
                } else if (plugin.getServer().getOfflinePlayer(args[1]) != null && plugin.getServer().getOfflinePlayer(args[1]).hasPlayedBefore()) {
                    OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[1]);

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(player, Messages.INVALID_AMOUNT.replace("%amount%", args[2]), true, true);
                        return true;
                    }

                    Methods.payOfflineGems(player, target, amount);
                } else Color.sendMessage(player, Messages.INVALID_PLAYER.replace("%player%", args[1]), true, true);
                return true;
            } else if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")) {
                if (!player.hasPermission(Permissions.GEMS_GIVE)) {
                    Color.sendMessage(player, Messages.NO_PERMISSION, true, true);
                    return true;
                }

                if (plugin.getServer().getPlayer(args[1]) != null) {
                    Player target = plugin.getServer().getPlayer(args[1]);

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(player, Messages.INVALID_AMOUNT.replace("%amount%", args[2]), true, true);
                        return true;
                    }

                    Methods.giveGems(target, amount);

                    Color.sendMessage(player, Messages.GEMS_GIVEN.replace("%gems%", Methods.format(amount)).replace("%player%", target.getName()), true, true);
                    if (message.contains("-s")) return true;
                    Color.sendMessage(target, Messages.GEMS_GAINED.replace("%gems%", Methods.format(amount)), true, true);
                } else if (plugin.getServer().getOfflinePlayer(args[1]) != null && plugin.getServer().getOfflinePlayer(args[1]).hasPlayedBefore()) {
                    OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[1]);

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(player, Messages.INVALID_AMOUNT.replace("%amount%", args[2]), true, true);
                        return true;
                    }

                    Methods.giveOfflineGems(target, amount);

                    Color.sendMessage(player, Messages.GEMS_GIVEN.replace("%gems%", Methods.format(amount)).replace("%player%", target.getName()), true, true);
                } else Color.sendMessage(player, Messages.INVALID_PLAYER.replace("%player%", args[1]), true, true);
                return true;
            } else if (args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")) {
                if (!player.hasPermission(Permissions.GEMS_TAKE)) {
                    Color.sendMessage(player, Messages.NO_PERMISSION, true, true);
                    return true;
                }

                if (plugin.getServer().getPlayer(args[1]) != null) {
                    Player target = plugin.getServer().getPlayer(args[1]);

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(player, Messages.INVALID_AMOUNT.replace("%amount%", args[2]), true, true);
                        return true;
                    }

                    if (!Methods.hasGems(target, amount)) {
                        Color.sendMessage(player, Messages.INVALID_GEMS.replace("%player%", target.getName()), true, false);
                        return true;
                    }

                    Methods.takeGems(target, amount);

                    Color.sendMessage(player, Messages.GEMS_TAKEN.replace("%gems%", Methods.format(amount)).replace("%player%", target.getName()), true, true);
                    if (message.contains("-s")) return true;
                    Color.sendMessage(target, Messages.GEMS_LOST.replace("%gems%", Methods.format(amount)), true, true);
                } else if (plugin.getServer().getOfflinePlayer(args[1]) != null && plugin.getServer().getOfflinePlayer(args[1]).hasPlayedBefore()) {
                    OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[1]);

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(player, Messages.INVALID_AMOUNT.replace("%amount%", args[2]), true, true);
                        return true;
                    }

                    if (!Methods.hasOfflineGems(target, amount)) {
                        Color.sendMessage(player, Messages.INVALID_GEMS.replace("%player%", target.getName()), true, false);
                        return true;
                    }

                    Methods.takeOfflineGems(target, amount);

                    Color.sendMessage(player, Messages.GEMS_TAKEN.replace("%gems%", Methods.format(amount)).replace("%player%", target.getName()), true, true);
                } else Color.sendMessage(player, Messages.INVALID_PLAYER.replace("%player%", args[1]), true, true);
                return true;
            } else if (args[0].equalsIgnoreCase("set")) {
                if (!player.hasPermission(Permissions.GEMS_SET)) {
                    Color.sendMessage(player, Messages.NO_PERMISSION, true, true);
                    return true;
                }

                if (plugin.getServer().getPlayer(args[1]) != null) {
                    Player target = plugin.getServer().getPlayer(args[1]);

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(player, Messages.INVALID_AMOUNT.replace("%amount%", args[2]), true, true);
                        return true;
                    }

                    Methods.setGems(target, amount);

                    Color.sendMessage(player, Messages.GEMS_SET.replace("%gems%", Methods.format(amount)).replace("%player%", target.getName()), true, true);
                    if (message.contains("-s")) return true;
                    Color.sendMessage(target, Messages.GEMS_SETTED.replace("%gems%", Methods.format(amount)), true, true);
                } else if (plugin.getServer().getOfflinePlayer(args[1]) != null && plugin.getServer().getOfflinePlayer(args[1]).hasPlayedBefore()) {
                    OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[1]);

                    double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Color.sendMessage(player, Messages.INVALID_AMOUNT.replace("%amount%", args[2]), true, true);
                        return true;
                    }

                    Methods.setOfflineGems(target, amount);

                    Color.sendMessage(player, Messages.GEMS_SET.replace("%gems%", Methods.format(amount)).replace("%player%", target.getName()), true, true);
                } else Color.sendMessage(player, Messages.INVALID_PLAYER.replace("%player%", args[1]), true, true);
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return (args.length == 1) ? StringUtil.copyPartialMatches(args[0], COMMANDS, new ArrayList<>()) : null;
    }
}