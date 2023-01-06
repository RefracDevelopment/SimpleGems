package me.refracdevelopment.simplegems.utilities;

import com.cryptomorin.xseries.XMaterial;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.data.DataType;
import me.refracdevelopment.simplegems.data.Profile;
import me.refracdevelopment.simplegems.data.ProfileData;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.files.Config;
import me.refracdevelopment.simplegems.utilities.files.Files;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

public class Methods {

    /**
     * The #saveOffline method allows you to
     * save a specified player's data
     */
    public static void saveOffline(OfflinePlayer player, long amount) {
        Bukkit.getScheduler().runTaskAsynchronously(SimpleGems.getInstance(), () -> {
            if (SimpleGems.getInstance().getDataType() == DataType.MYSQL) {
                SimpleGems.getInstance().getSqlManager().execute("UPDATE SimpleGems SET gems=? WHERE uuid=?",
                        amount, player.getUniqueId().toString());
            } else if (SimpleGems.getInstance().getDataType() == DataType.YAML) {
                Files.getData().set("data." + player.getUniqueId().toString() + ".gems", amount);
                Files.saveData();
            }
        });
    }

    public static void setOfflineGems(OfflinePlayer player, long amount) {
        saveOffline(player, amount);
    }

    public static void giveOfflineGems(OfflinePlayer player, long amount) {
        setOfflineGems(player, getOfflineGems(player) + amount);
    }
    
    public static void takeOfflineGems(OfflinePlayer player, long amount) {
        setOfflineGems(player, getOfflineGems(player) - amount);
    }

    public static long getOfflineGems(OfflinePlayer player) {
        if (SimpleGems.getInstance().getDataType() == DataType.MYSQL) {
            AtomicLong gems = new AtomicLong();
            Bukkit.getScheduler().runTaskAsynchronously(SimpleGems.getInstance(), () -> {
                SimpleGems.getInstance().getSqlManager().select("SELECT * FROM SimpleGems WHERE uuid=?", resultSet -> {
                    try {
                        if (resultSet.next()) {
                            gems.set(resultSet.getLong("gems"));
                        }
                    } catch (SQLException exception) {
                        Color.log(exception.getMessage());
                    }
                }, player.getUniqueId().toString());
            });
            return gems.get();
        } else if (SimpleGems.getInstance().getDataType() == DataType.YAML) {
            return Files.getData().getLong("data." + player.getUniqueId().toString() + ".gems");
        }
        return 0;
    }
    
    public static boolean hasOfflineGems(OfflinePlayer player, long amount) {
        return getOfflineGems(player) >= amount;
    }

    public static void payGems(Player player, Player target, long amount, boolean silent) {
        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();
        ProfileData targetProfile = SimpleGems.getInstance().getProfileManager().getProfile(target.getUniqueId()).getData();
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addPlaceholder("gems", String.valueOf(amount))
                .addAll(Placeholders.setPlaceholders(target))
                .build();

        if (player == target) {
            locale.sendCustomMessage(player, "&cYou can't pay yourself.");
            return;
        }

        if (profile.getGems().hasAmount(amount)) {
            profile.getGems().decrementAmount(amount);
            targetProfile.getGems().incrementAmount(amount);
            Bukkit.getScheduler().runTaskAsynchronously(SimpleGems.getInstance(), () -> {
                profile.save(player);
                targetProfile.save(target);
            });

            locale.sendMessage(player, "gems-paid", placeholders);
            if (silent) return;
            locale.sendMessage(target, "gems-received", placeholders);
        } else {
            locale.sendMessage(player, "not-enough-pay", placeholders);
        }
    }

    public static void payOfflineGems(Player player, OfflinePlayer target, long amount) {
        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addPlaceholder("player", target.getName())
                .addPlaceholder("gems", String.valueOf(amount))
                .addAll(Placeholders.setPlaceholders(player))
                .build();

        if (profile.getGems().hasAmount(amount)) {
            profile.getGems().decrementAmount(amount);
            giveOfflineGems(target, amount);

            locale.sendMessage(player, "gems-paid", placeholders);
        } else {
            locale.sendMessage(player, "not-enough-pay", placeholders);
        }
    }

    public static void withdrawGems(Player player, int amount) {
        if (amount > 2304) return; // Used to reduce lag

        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .addPlaceholder("gems", String.valueOf(amount))
                .build();

        if (profile.getData().getGems().hasAmount(amount)) {
            giveGemsItem(player, amount);
            profile.getData().getGems().decrementAmount(amount);

            locale.sendMessage(player, "gems-withdrawn", placeholders);
        } else {
            locale.sendMessage(player, "not-enough-withdraw", placeholders);
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
        ItemBuilder item = new ItemBuilder(material.parseMaterial());

        if (Config.GEMS_ITEM_CUSTOM_DATA) {
            if (Config.GEMS_ITEM_GLOW) {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                ItemMeta itemMeta = item.toItemStack().getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.toItemStack().setItemMeta(itemMeta);
            }

            if (NMSUtil.getVersionNumber() > 13) {
                item.setCustomModelData(Config.GEMS_ITEM_CUSTOM_MODEL_DATA);
            } else {
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
            }
            item.setName(name);
            lore.forEach(s -> item.addLoreLine(Color.translate(s)));
            item.setDurability(data);
        } else {
            if (Config.GEMS_ITEM_GLOW) {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                ItemMeta itemMeta = item.toItemStack().getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.toItemStack().setItemMeta(itemMeta);
            }

            item.setName(name);
            lore.forEach(s -> item.addLoreLine(Color.translate(s)));
            item.setDurability(data);
        }

        return item.toItemStack();
    }

    public static String formatDec(long amount) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(amount);
    }

    public static String format(long amount) {
        String fin = "none";
        if (amount <= 0.0) {
            fin = String.valueOf(0);
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