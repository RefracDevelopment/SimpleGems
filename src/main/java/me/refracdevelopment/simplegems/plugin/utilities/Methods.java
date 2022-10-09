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
package me.refracdevelopment.simplegems.plugin.utilities;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import com.cryptomorin.xseries.XMaterial;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.manager.Profile;
import me.refracdevelopment.simplegems.plugin.manager.database.DataType;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Color;
import me.refracdevelopment.simplegems.plugin.utilities.files.Config;
import me.refracdevelopment.simplegems.plugin.utilities.files.Files;
import me.refracdevelopment.simplegems.plugin.utilities.files.Menus;
import me.refracdevelopment.simplegems.plugin.utilities.files.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class Methods {

    /**
     * This will save all online player's data.
     */
    public static void saveTask() {
        SimpleGems.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(SimpleGems.getInstance(), () -> {
            SimpleGems.getInstance().getServer().getOnlinePlayers().forEach(player -> {
                SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData().save();
            });
        }, 2L, 2L);
    }

    /**
     * The #saveOffline method allows you to
     * save a specified player's data
     */
    public static void saveOffline(OfflinePlayer player, double amount) {
        if (SimpleGems.getInstance().getDataType() == DataType.MYSQL) {
            SimpleGems.getInstance().getSqlManager().execute("INSERT INTO simplegems (name,uuid,gems) VALUES (?,?,?) ON DUPLICATE KEY UPDATE name=?,gems=?",

                    // INSERT
                    player.getName(),
                    player.getUniqueId().toString(),
                    amount,
                    // UPDATE
                    player.getName(),
                    amount

            );
        } else if (SimpleGems.getInstance().getDataType() == DataType.YAML) {
            Files.getData().set("data." + player.getUniqueId().toString() + ".name", player.getName());
            Files.getData().set("data." + player.getUniqueId().toString() + ".gems", amount);
            Files.saveData();
        }
    }

    public static void getTop10(Player player) {
        Map<String, Double> gemsList = new HashMap<>();

        if (SimpleGems.getInstance().getDataType() == DataType.MYSQL) {
            SimpleGems.getInstance().getSqlManager().select("SELECT * FROM simplegems ORDER BY gems DESC LIMIT " +
                    Config.GEMS_TOP_ENTRIES, resultSet -> {
                try {
                    while (resultSet.next()) {
                        gemsList.put(resultSet.getString("name"), resultSet.getDouble("gems"));
                    }
                } catch (SQLException exception) {
                    Color.log(exception.getMessage());
                }
            });

            ValueComparator<String> vc = new ValueComparator<>(gemsList);
            TreeMap<String, Double> sorted = new TreeMap<>(vc);
            sorted.putAll(gemsList);

            Color.sendMessage(player, Config.GEMS_TOP_TITLE.replace("%entries%", String.valueOf(Config.GEMS_TOP_ENTRIES)), true, true);
            for (int i = 0; i < sorted.size(); ++i) {
                Map.Entry<String, Double> e = sorted.pollFirstEntry();
                Color.sendMessage(player, Config.GEMS_TOP_FORMAT.replace("%number%", String.valueOf(i + 1)).replace("%player%", e.getKey())
                        .replace("%value%", format(e.getValue())).replace("%value_decimal%", formatDec(e.getValue())), true, true);
            }
        } else if (SimpleGems.getInstance().getDataType() == DataType.YAML) {
            for (Player p : SimpleGems.getInstance().getServer().getOnlinePlayers()) {
                Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(p.getUniqueId());
                String name = profile.getData().getName();
                double gems = profile.getData().getGems().getStat();
                gemsList.put(name, gems);
            }

            for (OfflinePlayer op : SimpleGems.getInstance().getServer().getOfflinePlayers()) {
                String name = op.getName();
                double gems = getOfflineGems(op);
                gemsList.put(name, gems);
            }

            ValueComparator<String> vc = new ValueComparator<>(gemsList);
            TreeMap<String, Double> sorted = new TreeMap<>(vc);
            sorted.putAll(gemsList);

            Color.sendMessage(player, Config.GEMS_TOP_TITLE.replace("%entries%", String.valueOf(Config.GEMS_TOP_ENTRIES)), true, true);
            for (int i = 0; i < sorted.entrySet().stream().limit(Config.GEMS_TOP_ENTRIES).count(); ++i) {
                Map.Entry<String, Double> e = sorted.pollFirstEntry();
                Color.sendMessage(player, Config.GEMS_TOP_FORMAT.replace("%number%", String.valueOf(i + 1)).replace("%player%", e.getKey())
                        .replace("%value%", format(e.getValue())).replace("%value_decimal%", formatDec(e.getValue())), true, true);
            }
        }
    }

    public static void setOfflineGems(OfflinePlayer player, double amount) {
        saveOffline(player, amount);
    }

    public static void giveOfflineGems(OfflinePlayer player, double amount) {
        setOfflineGems(player, getOfflineGems(player) + amount);
    }
    
    public static void takeOfflineGems(OfflinePlayer player, double amount) {
        setOfflineGems(player, getOfflineGems(player) - amount);
    }

    public static double getOfflineGems(OfflinePlayer player) {
        if (SimpleGems.getInstance().getDataType() == DataType.MYSQL) {
            AtomicReference<Double> gems = new AtomicReference<>(0.0);
            SimpleGems.getInstance().getSqlManager().select("SELECT * FROM simplegems WHERE uuid=?", resultSet -> {
                try {
                    if (resultSet.next()) {
                        gems.set(resultSet.getDouble("gems"));
                    }
                } catch (SQLException exception) {
                    Color.log(exception.getMessage());
                }
            }, player.getUniqueId().toString());
            return gems.get();
        } else if (SimpleGems.getInstance().getDataType() == DataType.YAML) {
            return Files.getData().getDouble("data." + player.getUniqueId().toString() + ".gems");
        }
        return 0.0;
    }
    
    public static boolean hasOfflineGems(OfflinePlayer player, double amount) {
        return getOfflineGems(player) >= amount;
    }

    public static void payGems(Player player, Player target, double amount, boolean silent) {
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Profile targetProfile = SimpleGems.getInstance().getProfileManager().getProfile(target.getUniqueId());

        if (target == player) {
            Color.sendMessage(player, "&cYou can't pay yourself.", true, true);
            return;
        }

        if (profile.getData().getGems().hasStat(amount)) {
            profile.getData().getGems().decrementStat(amount);
            targetProfile.getData().getGems().incrementStat(amount);

            Color.sendMessage(player, Messages.GEMS_PAID.replace("%player%", target.getName()).replaceAll("%gems%", amount + ""), true, true);
            if (silent) return;
            Color.sendMessage(target, Messages.GEMS_RECEIVED.replace("%player%", player.getName()).replaceAll("%gems%", amount + ""), true, true);
        } else {
            player.sendMessage(Color.translate(player, Messages.NOT_ENOUGH_PAY));
        }
    }

    public static void payOfflineGems(Player player, OfflinePlayer target, double amount) {
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getData().getGems().hasStat(amount)) {
            profile.getData().getGems().decrementStat(amount);
            giveOfflineGems(target, amount);

            Color.sendMessage(player, Messages.GEMS_PAID.replace("%player%", target.getName()).replace("%gems%", Methods.format(amount))
                    .replace("%gems_decimal%", Methods.formatDec(amount)), true, true);
        } else {
            Color.sendMessage(player, Messages.NOT_ENOUGH_PAY.replace("%player%", target.getName()).replace("%gems%", Methods.format(amount))
                    .replace("%gems_decimal%", Methods.formatDec(amount)), true, true);
        }
    }

    public static void withdrawGems(Player player, int amount) {
        if (amount > 2304) return; // Used to reduce lag

        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getData().getGems().hasStat(amount)) {
            giveGemsItem(player, amount);
            profile.getData().getGems().decrementStat(amount);

            Color.sendMessage(player, Messages.GEMS_WITHDRAWN.replace("%gems%", format(amount)).replace("%gems_decimal%", formatDec(amount)), true, true);
        } else {
            Color.sendMessage(player, Messages.NOT_ENOUGH_WITHDRAW, true, true);
        }
    }

    public static void giveGemsItem(Player player, int amount) {
        ItemStack gemsItem = getGemsItem();
        int stacksToGive = 0;
        while (amount > 64) {
            ++stacksToGive;
            amount -= 64;
        }
        while (stacksToGive > 0) {
            gemsItem.setAmount(64);
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().setItem(player.getInventory().firstEmpty(), gemsItem);
                player.updateInventory();
            } else {
                player.getWorld().dropItem(player.getLocation(), gemsItem);
            }
            --stacksToGive;
        }
        gemsItem.setAmount(amount);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().setItem(player.getInventory().firstEmpty(), gemsItem);
            player.updateInventory();
        } else {
            player.getWorld().dropItem(player.getLocation(), gemsItem);
        }
    }

    /**
     * This will give gems in item form
     * received by doing events etc
     *
     * @return an item stack to redeem gems
     */
    public static ItemStack getGemsItem() {
        String name = Config.GEMS_ITEM_NAME;
        XMaterial material = XMaterial.matchXMaterial(Material.getMaterial(Config.GEMS_ITEM));
        int data = Config.GEMS_ITEM_DATA;
        List<String> lore = Config.GEMS_ITEM_LORE;
        if (Config.GEMS_ITEM_CUSTOM_DATA && Files.getConfig().contains("gems-item.custom-data")) {
            ItemBuilder item = new ItemBuilder(material.parseMaterial());

            if (Config.GEMS_ITEM_GLOW && Files.getConfig().contains("gems-item.glow")) {
                if (Bukkit.getVersion().contains("1.7")) {
                    item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                } else if (VersionCheck.isOnePointNinePlus()) {
                    item.addEnchant(SimpleGems.getInstance().getGlow(), 1);
                } else {
                    item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                    ItemMeta itemMeta = item.toItemStack().getItemMeta();
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.toItemStack().setItemMeta(itemMeta);
                }
            }

            if (VersionCheck.isOnePointFourteenPlus()) {
                item.setCustomModelData(Config.GEMS_ITEM_CUSTOM_MODEL_DATA);
            } else {
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
            }
            item.setName(name);
            item.setLore(lore);
            item.setDurability(data);

            return item.toItemStack();
        } else {
            ItemBuilder item = new ItemBuilder(material.parseMaterial());

            if (Config.GEMS_ITEM_GLOW && Files.getConfig().contains("gems-item.glow")) {
                if (Bukkit.getVersion().contains("1.7")) {
                    item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                } else if (VersionCheck.isOnePointNinePlus()) {
                    item.addEnchant(SimpleGems.getInstance().getGlow(), 1);
                } else {
                    item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                    ItemMeta itemMeta = item.toItemStack().getItemMeta();
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.toItemStack().setItemMeta(itemMeta);
                }
            }

            item.setName(name);
            item.setLore(lore);
            item.setDurability(data);

            return item.toItemStack();
        }
    }

    public static String formatDec(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(amount);
    }

    public static String format(double amount) {
        String fin = "none";
        if (amount <= 0.0) {
            fin = String.valueOf(0);
        } else if (amount >= 1.0E30) {
            fin = String.format("%.1fN", amount / 1.0E30);
        } else if (amount >= 1.0E27) {
            fin = String.format("%.1fO", amount / 1.0E27);
        } else if (amount >= 1.0E24) {
            fin = String.format("%.1fSP", amount / 1.0E24);
        } else if (amount >= 1.0E21) {
            fin = String.format("%.1fS", amount / 1.0E21);
        } else if (amount >= 1.0E18) {
            fin = String.format("%.1fQT", amount / 1.0E18);
        } else if (amount >= 1.0E15) {
            fin = String.format("%.1fQ", amount / 1.0E15);
        } else if (amount >= 1.0E12) {
            fin = String.format("%.1fT", amount / 1.0E12);
        } else if (amount >= 1.0E9) {
            fin = String.format("%.1fB", amount / 1.0E9);
        } else if (amount >= 1000000.0) {
            fin = String.format("%.1fM", amount / 1000000.0);
        } else if (amount >= 1000.0) {
            fin = String.format("%.1fK", amount / 1000.0);
        }
        if (fin.contains(".0")) {
            fin = fin.replace(".0", "");
        }
        if (fin.equals("none")) {
            return NumberFormat.getNumberInstance(Locale.US).format(amount);
        }
        return fin;
    }
}