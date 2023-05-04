package me.refracdevelopment.simplegems.utilities;

import com.cryptomorin.xseries.XMaterial;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.data.ProfileData;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Methods {

    /**
     * The #saveOffline method allows you to
     * save a specified player's data
     */
    public static void saveOffline(OfflinePlayer player, double amount) {
        Bukkit.getScheduler().runTaskAsynchronously(SimpleGems.getInstance(), () -> {
            SimpleGems.getInstance().getPlayerMapper().saveOfflinePlayer(player.getUniqueId(), player.getName(), amount);
        });
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
        return SimpleGems.getInstance().getPlayerMapper().getGems(player.getUniqueId());
    }
    
    public static boolean hasOfflineGems(OfflinePlayer player, double amount) {
        return getOfflineGems(player) >= amount;
    }

    public static void payGems(Player player, Player target, double amount, boolean silent) {
        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();
        ProfileData targetProfile = SimpleGems.getInstance().getProfileManager().getProfile(target.getUniqueId()).getData();
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .add("gems", String.valueOf(amount))
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
                profile.save();
                targetProfile.save();
            });

            locale.sendMessage(player, "gems-paid", placeholders);
            if (silent) return;
            locale.sendMessage(target, "gems-received", placeholders);
        } else {
            locale.sendMessage(player, "not-enough-pay", placeholders);
        }
    }

    public static void payOfflineGems(Player player, OfflinePlayer target, double amount) {
        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .add("player", target.getName())
                .add("gems", String.valueOf(amount))
                .add("gems_formatted", Methods.format(amount))
                .add("gems_decimal", Methods.formatDec(amount))
                .build();

        if (profile.getGems().hasAmount(amount)) {
            profile.getGems().decrementAmount(amount);
            giveOfflineGems(target, amount);

            locale.sendMessage(player, "gems-paid", placeholders);
        } else {
            locale.sendMessage(player, "not-enough-pay", placeholders);
        }
    }

    public static void withdrawGems(Player player, double amount) {
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .add("gems", String.valueOf(amount))
                .add("gems_formatted", Methods.format(amount))
                .add("gems_decimal", Methods.formatDec(amount))
                .build();

        if (SimpleGemsAPI.INSTANCE.hasGems(player, amount)) {
            giveGemsItem(player, amount);
            SimpleGemsAPI.INSTANCE.takeGems(player, amount);

            locale.sendMessage(player, "gems-withdrawn", placeholders);
        } else {
            locale.sendMessage(player, "not-enough-withdraw", placeholders);
        }
    }

    public static void giveGemsItem(Player player, double amount) {
        ItemStack gemsItem = getGemsItem(amount);

        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().setItem(player.getInventory().firstEmpty(), gemsItem);
            player.updateInventory();
        } else {
            player.getWorld().dropItem(player.getLocation(), gemsItem);
        }
    }

    public static ItemStack getGemsItem(double amount) {
        String name = Config.GEMS_ITEM_NAME;
        XMaterial material = Utilities.getMaterial(Config.GEMS_ITEM_MATERIAL);
        int data = Config.GEMS_ITEM_DATA;
        List<String> lore = Config.GEMS_ITEM_LORE;
        ItemBuilder item = new ItemBuilder(material.parseMaterial(), 1);
        ItemMeta itemMeta = item.toItemStack().getItemMeta();

        if (Config.GEMS_ITEM_CUSTOM_DATA) {
            if (Config.GEMS_ITEM_GLOW) {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            if (NMSUtil.getVersionNumber() > 13) {
                item.setCustomModelData(Config.GEMS_ITEM_CUSTOM_MODEL_DATA);
            } else {
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
            }

            // Attempt to insert a NBT tag of the gems value instead of filling the inventory

            NamespacedKey key = new NamespacedKey(SimpleGems.getInstance(), "gems-item-value");
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            container.set(key, PersistentDataType.DOUBLE, amount);
            item.toItemStack().setItemMeta(itemMeta);

            item.setName(name);

            if(container.has(key, PersistentDataType.DOUBLE)) {
                double foundValue = container.get(key, PersistentDataType.DOUBLE);
                lore.forEach(s -> item.addLoreLine(Color.translate(s
                        .replace("%value%", String.valueOf(foundValue))
                        .replace("%gems%", String.valueOf(foundValue))
                )));
            } else {
                lore.forEach(s -> item.addLoreLine(Color.translate(s
                        .replace("%value%", String.valueOf(item.toItemStack().getAmount()))
                        .replace("%gems%", String.valueOf(item.toItemStack().getAmount()))
                )));
            }
            item.setDurability(data);
        } else {
            if (Config.GEMS_ITEM_GLOW) {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            // Attempt to insert a NBT tag of the gems value instead of filling the inventory

            NamespacedKey key = new NamespacedKey(SimpleGems.getInstance(), "gems-item-value");
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            container.set(key, PersistentDataType.DOUBLE, amount);
            item.toItemStack().setItemMeta(itemMeta);

            item.setName(name);

            if(container.has(key, PersistentDataType.DOUBLE)) {
                double foundValue = container.get(key, PersistentDataType.DOUBLE);
                lore.forEach(s -> item.addLoreLine(Color.translate(s
                        .replace("%value%", String.valueOf(foundValue))
                        .replace("%gems%", String.valueOf(foundValue))
                )));
            } else {
                lore.forEach(s -> item.addLoreLine(Color.translate(s
                        .replace("%value%", String.valueOf(item.toItemStack().getAmount()))
                        .replace("%gems%", String.valueOf(item.toItemStack().getAmount()))
                )));
            }
            item.setDurability(data);
        }

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
            fin = String.format("%.1fST", amount / 1.0E24);
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
        } else if (amount >= 1.0E6) {
            fin = String.format("%.1fM", amount / 1.0E6);
        } else if (amount >= 1.0E3) {
            fin = String.format("%.1fK", amount / 1.0E3);
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