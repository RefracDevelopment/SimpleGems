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

import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.manager.Profile;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Color;
import me.refracdevelopment.simplegems.plugin.utilities.files.Config;
import me.refracdevelopment.simplegems.plugin.utilities.files.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class Methods {

    /**
     * This will save all online player's data.
     */
    public static void saveTask() {
        if (!Config.AUTO_SAVE_ENABLED) return;
        Bukkit.getScheduler().runTaskTimerAsynchronously(SimpleGems.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData().save();
                Color.sendMessage(player, Messages.SAVE_BROADCAST, true, true);
            }
        }, Config.AUTO_SAVE_INTERVAL*20L, Config.AUTO_SAVE_INTERVAL*20L);
    }

    /**
     * The #saveOffline method allows you to
     * save a specified player's data
     */
    public static void saveOffline(OfflinePlayer player, double amount) {
        try {
            PreparedStatement statement = SimpleGems.getInstance().getSqlManager().getConnection().prepareStatement("SELECT * FROM simplegems WHERE uuid=?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                PreparedStatement update = SimpleGems.getInstance().getSqlManager().getConnection().prepareStatement("UPDATE simplegems SET "
                        + "name=?,uuid=?,gems=? WHERE uuid=?");

                update.setString(1, player.getName());
                update.setString(2, player.getUniqueId().toString());
                update.setDouble(3, amount);
                update.setString(4, player.getUniqueId().toString());

                update.executeUpdate();
                update.close();
            } else {
                PreparedStatement insert = SimpleGems.getInstance().getSqlManager().getConnection().prepareStatement("INSERT INTO simplegems ("
                        + "name,uuid,gems) VALUES ("
                        + "?,?,?)");

                insert.setString(1, player.getName());
                insert.setString(2, player.getUniqueId().toString());
                insert.setDouble(3, amount);

                insert.executeUpdate();
                insert.close();
            }

            SimpleGems.getInstance().getSqlManager().close(statement, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getTop10(Player player) {
        Map<String, Double> gemsList = new HashMap<>();

        try {
            PreparedStatement statement = SimpleGems.getInstance().getSqlManager().getConnection().prepareStatement("SELECT * FROM simplegems ORDER BY gems DESC LIMIT " +
                    Config.GEMS_TOP_ENTRIES);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String name = result.getString("name");
                double gems = result.getDouble("gems");
                gemsList.put(name, gems);
            }

            ValueComparator<String> vc = new ValueComparator<>(gemsList);
            TreeMap<String, Double> sorted = new TreeMap<>(vc);
            sorted.putAll(gemsList);

            Color.sendMessage(player, Config.GEMS_TOP_TITLE.replace("%entries%", String.valueOf(Config.GEMS_TOP_ENTRIES)), true, true);
            for (int i = 0; i < sorted.size(); ++i) {
                Map.Entry<String, Double> e = sorted.pollFirstEntry();
                Color.sendMessage(player, Config.GEMS_TOP_FORMAT.replace("%number%", String.valueOf(i + 1)).replace("%player%", e.getKey())
                        .replace("%value%", format(e.getValue())).replace("%value_decimal%", formatDec(e.getValue())), true, true);
            }

            SimpleGems.getInstance().getSqlManager().close(statement, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setGems(Player player, double amount) {
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            profile.getData().getGems().put(player.getUniqueId(), amount);
        }
    }
    
    public static void setOfflineGems(OfflinePlayer player, double amount) {
        Bukkit.getScheduler().runTaskAsynchronously(SimpleGems.getInstance(), () -> saveOffline(player, amount));
    }
    
    public static void giveGems(Player player, double amount) {
        setGems(player, getGems(player) + amount);
    }

    public static void giveOfflineGems(OfflinePlayer player, double amount) {
        setOfflineGems(player, getOfflineGems(player) + amount);
    }

    public static void takeGems(Player player, double amount) {
        setGems(player, getGems(player) - amount);
    }
    
    public static void takeOfflineGems(OfflinePlayer player, double amount) {
        setOfflineGems(player, getOfflineGems(player) - amount);
    }
    
    public static double getGems(Player player) {
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

        return profile.getData().getGems().getOrDefault(player.getUniqueId(), 0.0);
    }

    public static double getOfflineGems(OfflinePlayer player) {
        try {
            PreparedStatement statement = SimpleGems.getInstance().getSqlManager().getConnection().prepareStatement("SELECT * FROM simplegems WHERE uuid=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getDouble("gems");
            }

            SimpleGems.getInstance().getSqlManager().close(statement, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static boolean hasGems(Player player, double amount) {
        return getGems(player) >= amount;
    }
    
    public static boolean hasOfflineGems(OfflinePlayer player, double amount) {
        return getOfflineGems(player) >= amount;
    }

    public static void payGems(Player player, Player target, double amount, boolean silent) {
        if (hasGems(player, amount)) {
            takeGems(player, amount);
            giveGems(target, amount);

            if (silent) return;
            Color.sendMessage(player, Messages.GEMS_PAID.replace("%player%", target.getName()).replaceAll("%gems%", amount + ""), true, true);
            Color.sendMessage(target, Messages.GEMS_RECEIVED.replace("%player%", player.getName()).replaceAll("%gems%", amount + ""), true, true);
        } else {
            player.sendMessage(Color.translate(player, Messages.NOT_ENOUGH_PAY));
        }
    }

    public static void payOfflineGems(Player player, OfflinePlayer target, double amount) {
        Bukkit.getScheduler().runTaskAsynchronously(SimpleGems.getInstance(), () -> {
            if (hasGems(player, amount)) {
                takeGems(player, amount);
                giveOfflineGems(target, amount);

                Color.sendMessage(player, Messages.GEMS_PAID.replace("%player%", target.getName()).replace("%gems%", Methods.format(amount))
                        .replace("%gems_decimal%", Methods.formatDec(amount)), true, true);
            } else {
                Color.sendMessage(player, Messages.NOT_ENOUGH_PAY.replace("%player%", target.getName()).replace("%gems%", Methods.format(amount))
                        .replace("%gems_decimal%", Methods.formatDec(amount)), true, true);
            }
        });
    }

    public static void withdrawGems(Player player, int amount) {
        if (amount > 2304) return; // Used to reduce lag

        if (hasGems(player, amount)) {
            giveGemsItem(player, amount);
            takeGems(player, amount);

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

    public static ItemStack getGemsItem() {
        String name = Config.GEMS_ITEM_NAME;
        Material material = Config.GEMS_ITEM;
        int data = Config.GEMS_ITEM_DATA;
        ItemBuilder item = new ItemBuilder(material);

        item.setName(Color.translate(name));
        for (String s : Config.GEMS_ITEM_LORE)
            item.addLoreLine(Color.translate(s));

        item.setDurability(data);

        return item.toItemStack();
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