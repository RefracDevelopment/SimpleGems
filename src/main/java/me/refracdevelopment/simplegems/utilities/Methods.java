package me.refracdevelopment.simplegems.utilities;

import com.cryptomorin.xseries.XMaterial;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.manager.data.DataType;
import me.refracdevelopment.simplegems.manager.data.Profile;
import me.refracdevelopment.simplegems.manager.data.ProfileData;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.files.Config;
import me.refracdevelopment.simplegems.utilities.files.Files;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Methods {

    /**
     * This will save all online player's data.
     */
    public static void saveTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SimpleGems.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
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
            SimpleGems.getInstance().getManager(Files.class).saveData();
        }
    }

    public static void getTop10(Player player) {
        Map<String, Double> gemsList = new HashMap<>();

        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        if (SimpleGems.getInstance().getDataType() == DataType.MYSQL) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(p.getUniqueId());

                if (profile != null) {
                    gemsList.put(profile.getPlayerName(), profile.getData().getGems().getStat());
                }
            });

            SimpleGems.getInstance().getSqlManager().select("SELECT * FROM simplegems ORDER BY gems", resultSet -> {
                try {
                    while (resultSet.next()) {
                        gemsList.put(resultSet.getString("name"), resultSet.getDouble("gems"));
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });

            ValueComparator<String> vc = new ValueComparator<>(gemsList);
            TreeMap<String, Double> sorted = new TreeMap<>(vc);
            sorted.putAll(gemsList);

            locale.sendCustomMessage(player, Placeholders.setPlaceholders(player, Config.GEMS_TOP_TITLE.replace("%entries%", String.valueOf(Config.GEMS_TOP_ENTRIES))));
            try {
                for (int i = 0; i <= Config.GEMS_TOP_ENTRIES; i++) {
                    Map.Entry<String, Double> e = sorted.pollFirstEntry();
                    locale.sendCustomMessage(player, Placeholders.setPlaceholders(player, Config.GEMS_TOP_FORMAT.replace("%number%", String.valueOf(i + 1)).replace("%player%", e.getKey())
                            .replace("%value%", format(e.getValue())).replace("%value_decimal%", formatDec(e.getValue()))));
                }
            } catch (Exception ignored) {}
        } else if (SimpleGems.getInstance().getDataType() == DataType.YAML) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(p.getUniqueId());

                if (profile != null) {
                    gemsList.put(profile.getData().getName(), profile.getData().getGems().getStat());
                }
            });

            for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                String name = op.getName();
                double gems = getOfflineGems(op);
                gemsList.put(name, gems);
            }

            ValueComparator<String> vc = new ValueComparator<>(gemsList);
            TreeMap<String, Double> sorted = new TreeMap<>(vc);
            sorted.putAll(gemsList);

            locale.sendCustomMessage(player, Placeholders.setPlaceholders(player, Config.GEMS_TOP_TITLE.replace("%entries%", String.valueOf(Config.GEMS_TOP_ENTRIES))));
            try {
                for (int i = 0; i <= Config.GEMS_TOP_ENTRIES; i++) {
                    Map.Entry<String, Double> e = sorted.pollFirstEntry();
                    locale.sendCustomMessage(player, Placeholders.setPlaceholders(player, Config.GEMS_TOP_FORMAT.replace("%number%", String.valueOf(i + 1)).replace("%player%", e.getKey())
                            .replace("%value%", format(e.getValue())).replace("%value_decimal%", formatDec(e.getValue()))));
                }
            } catch (Exception ignored) {}
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
                    exception.printStackTrace();
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
        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();
        ProfileData targetProfile = SimpleGems.getInstance().getProfileManager().getProfile(target.getUniqueId()).getData();
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        if (player == target) {
            locale.sendCustomMessage(player, "&cYou can't pay yourself.");
            return;
        }

        if (profile.getGems().hasStat(amount)) {
            profile.getGems().decrementStat(amount);
            targetProfile.getGems().incrementStat(amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addPlaceholder("gems", String.valueOf(amount))
                    .addAll(Placeholders.setPlaceholders(target))
                    .build();

            locale.sendMessage(player, "gems-paid", placeholders);
            if (silent) return;
            locale.sendMessage(target, "gems-received", placeholders);
        } else {
            locale.sendMessage(player, "not-enough-pay");
        }
    }

    public static void payOfflineGems(Player player, OfflinePlayer target, double amount) {
        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        if (profile.getGems().hasStat(amount)) {
            profile.getGems().decrementStat(amount);
            giveOfflineGems(target, amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addPlaceholder("player", target.getName())
                    .addPlaceholder("gems", String.valueOf(amount))
                    .addAll(Placeholders.setPlaceholders(player))
                    .build();

            locale.sendMessage(player, "gems-paid", placeholders);
        } else {
            locale.sendMessage(player, "not-enough-pay");
        }
    }

    public static void withdrawGems(Player player, int amount) {
        if (amount > 2304) return; // Used to reduce lag

        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        if (profile.getData().getGems().hasStat(amount)) {
            giveGemsItem(player, amount);
            profile.getData().getGems().decrementStat(amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(player))
                    .addPlaceholder("gems", String.valueOf(amount))
                    .build();

            locale.sendMessage(player, "gems-withdrawn", placeholders);
        } else {
            locale.sendMessage(player, "not-enough-withdraw");
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
        XMaterial material = Utilities.getMaterial(Config.GEMS_ITEM);
        int data = Config.GEMS_ITEM_DATA;
        List<String> lore = Config.GEMS_ITEM_LORE;
        if (Config.GEMS_ITEM_CUSTOM_DATA) {
            ItemBuilder item = new ItemBuilder(material.parseMaterial());

            if (Config.GEMS_ITEM_GLOW) {
                item.addEnchant(SimpleGems.getInstance().getGlow(), 1);
            }

            if (NMSUtil.getVersionNumber() > 13) {
                item.setCustomModelData(Config.GEMS_ITEM_CUSTOM_MODEL_DATA);
            } else {
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
            }
            item.setName(name);
            lore.forEach(s -> item.addLoreLine(Color.translate(s)));
            item.setDurability(data);

            return item.toItemStack();
        } else {
            ItemBuilder item = new ItemBuilder(material.parseMaterial());

            if (Config.GEMS_ITEM_GLOW) {
                item.addEnchant(SimpleGems.getInstance().getGlow(), 1);
            }

            item.setName(name);
            lore.forEach(s -> item.addLoreLine(Color.translate(s)));
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