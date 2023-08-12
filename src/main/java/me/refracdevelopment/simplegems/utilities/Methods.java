package me.refracdevelopment.simplegems.utilities;

import com.cryptomorin.xseries.XMaterial;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import de.tr7zw.nbtapi.NBTItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.manager.configuration.LocaleManager;
import me.refracdevelopment.simplegems.manager.configuration.cache.Config;
import me.refracdevelopment.simplegems.manager.data.DataType;
import me.refracdevelopment.simplegems.player.data.ProfileData;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bson.Document;
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
            if (SimpleGems.getInstance().getDataType() == DataType.MONGO) {
                Document document = new Document();
                document.put("name", player.getName());
                document.put("uuid", player.getUniqueId().toString());
                document.put("gems", amount);

                SimpleGems.getInstance().getMongoManager().getStatsCollection().replaceOne(Filters.eq("uuid", player.getUniqueId().toString()), document, new ReplaceOptions().upsert(true));
            } else if (SimpleGems.getInstance().getDataType() == DataType.MYSQL) {
                SimpleGems.getInstance().getMySQLManager().execute("UPDATE SimpleGems SET gems=? WHERE uuid=?",
                        amount, player.getUniqueId().toString());
            } else if (SimpleGems.getInstance().getDataType() == DataType.FLAT_FILE) {
                SimpleGems.getInstance().getPlayerMapper().savePlayer(player.getUniqueId(), player.getName(), amount);
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
        if (SimpleGems.getInstance().getDataType() == DataType.MONGO) {
            Document document = SimpleGems.getInstance().getMongoManager().getStatsCollection().find(Filters.eq("uuid", player.getUniqueId().toString())).first();

            if (document != null) {
                return document.getLong("gems");
            }
        } else if (SimpleGems.getInstance().getDataType() == DataType.MYSQL) {
            AtomicLong gems = new AtomicLong(0);
            SimpleGems.getInstance().getMySQLManager().select("SELECT * FROM SimpleGems WHERE uuid=?", resultSet -> {
                try {
                    if (resultSet.next()) {
                        gems.set(resultSet.getLong("gems"));
                    }
                } catch (SQLException exception) {
                    Color.log(exception.getMessage());
                }
            }, player.getUniqueId().toString());
            return gems.get();
        } else if (SimpleGems.getInstance().getDataType() == DataType.FLAT_FILE) {
            return SimpleGems.getInstance().getPlayerMapper().getGems(player.getUniqueId());
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

    public static void payOfflineGems(Player player, OfflinePlayer target, long amount) {
        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .add("player", target.getName())
                .add("gems", String.valueOf(amount))
                .add("gems_formatted", Methods.format(amount))
                .add("gems_decimal", Methods.formatDecimal(amount))
                .build();

        if (profile.getGems().hasAmount(amount)) {
            profile.getGems().decrementAmount(amount);
            giveOfflineGems(target, amount);

            locale.sendMessage(player, "gems-paid", placeholders);
        } else {
            locale.sendMessage(player, "not-enough-pay", placeholders);
        }
    }

    public static void withdrawGems(Player player, long amount) {
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .add("gems", String.valueOf(amount))
                .add("gems_formatted", Methods.format(amount))
                .add("gems_decimal", Methods.formatDecimal(amount))
                .build();

        if (SimpleGemsAPI.INSTANCE.hasGems(player, amount)) {
            giveGemsItem(player, amount);
            SimpleGemsAPI.INSTANCE.takeGems(player, amount);

            locale.sendMessage(player, "gems-withdrawn", placeholders);
        } else {
            locale.sendMessage(player, "not-enough-withdraw", placeholders);
        }
    }

    public static void giveGemsItem(Player player, long amount) {
        ItemStack gemsItem = getGemsItem(amount);

        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().setItem(player.getInventory().firstEmpty(), gemsItem);
            player.updateInventory();
        } else {
            player.getWorld().dropItem(player.getLocation(), gemsItem);
        }
    }

    public static ItemStack getGemsItem(long amount) {
        String name = Config.GEMS_ITEM_NAME;
        XMaterial material = Utilities.getMaterial(Config.GEMS_ITEM_MATERIAL);
        int data = Config.GEMS_ITEM_DATA;
        List<String> lore = Config.GEMS_ITEM_LORE;
        ItemBuilder item = new ItemBuilder(material.parseMaterial(), 1);

        if (Config.GEMS_ITEM_GLOW) {
            item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
            ItemMeta itemMeta = item.toItemStack().getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.toItemStack().setItemMeta(itemMeta);
        }

        if (Config.GEMS_ITEM_CUSTOM_DATA) {
            if (NMSUtil.getVersionNumber() >= 14) {
                item.setCustomModelData(Config.GEMS_ITEM_CUSTOM_MODEL_DATA);
            } else {
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
            }
        }

        item.setName(name);

        // Attempt to insert a NBT tag of the gems value instead of filling the inventory

        NBTItem nbtItem = new NBTItem(item.toItemStack());
        nbtItem.setLong("gems-item-value", amount);
        nbtItem.applyNBT(item.toItemStack());

        if(nbtItem.hasTag("gems-item-value")) {
            long foundValue = nbtItem.getLong("gems-item-value");
            lore.forEach(s -> item.addLoreLine(Color.translate(s
                    .replace("%value%", String.valueOf(foundValue))
                    .replace("%gems%", String.valueOf(foundValue))
            )));
        }

        item.setDurability(data);

        return item.toItemStack();
    }

    public static String formatDecimal(long amount) {
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