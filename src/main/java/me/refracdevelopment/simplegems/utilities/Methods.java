package me.refracdevelopment.simplegems.utilities;

import com.cryptomorin.xseries.ReflectionUtils;
import com.cryptomorin.xseries.XMaterial;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import de.tr7zw.nbtapi.NBTItem;
import lombok.experimental.UtilityClass;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.data.DataType;
import me.refracdevelopment.simplegems.player.data.ProfileData;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.chat.StringPlaceholders;
import org.bson.Document;
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
import java.util.UUID;

@UtilityClass
public class Methods {

    /**
     * The #saveOffline method allows you to
     * save a specified player's data
     */
    public void saveOffline(OfflinePlayer player, long amount) {
        Tasks.runAsync(wrappedTask -> {
            if (SimpleGems.getInstance().getDataType() == DataType.MONGO) {
                Document document = new Document();

                document.put("name", player.getName());
                document.put("uuid", player.getUniqueId().toString());
                document.put("gems", amount);

                SimpleGems.getInstance().getMongoManager().getStatsCollection().replaceOne(Filters.eq("uuid", player.getUniqueId().toString()), document, new ReplaceOptions().upsert(true));
            } else if (SimpleGems.getInstance().getDataType() == DataType.MYSQL) {
                try {
                    SimpleGems.getInstance().getMySQLManager().updatePlayerGems(player.getUniqueId(), amount);
                } catch (SQLException exception) {
                    Color.log("&cMySQL Error: " + exception.getMessage());
                }
            } else if (SimpleGems.getInstance().getDataType() == DataType.SQLITE) {
                try {
                    SimpleGems.getInstance().getSqLiteManager().updatePlayerGems(player.getUniqueId(), amount);
                } catch (SQLException exception) {
                    Color.log("&cSQLite Error: " + exception.getMessage());
                }
            } else if (SimpleGems.getInstance().getDataType() == DataType.FLAT_FILE) {
                SimpleGems.getInstance().getPlayerMapper().savePlayer(player.getUniqueId(), player.getName(), amount);
            }
        });
    }

    public void setOfflineGems(OfflinePlayer player, long amount) {
        saveOffline(player, amount);
    }

    public void giveOfflineGems(OfflinePlayer player, long amount) {
        setOfflineGems(player, getOfflineGems(player) + amount);
    }

    public void takeOfflineGems(OfflinePlayer player, long amount) {
        setOfflineGems(player, getOfflineGems(player) - amount);
    }

    public long getOfflineGems(OfflinePlayer player) {
        if (SimpleGems.getInstance().getDataType() == DataType.MONGO) {
            Document document = SimpleGems.getInstance().getMongoManager().getStatsCollection().find(Filters.eq("uuid", player.getUniqueId().toString())).first();

            if (document != null) {
                return document.getLong("gems");
            }
        } else if (SimpleGems.getInstance().getDataType() == DataType.MYSQL) {
            try {
                return SimpleGems.getInstance().getMySQLManager().getPlayerGems(player.getUniqueId()).getGems();
            } catch (SQLException exception) {
                Color.log("&cMySQL Error: " + exception.getMessage());
            }
        } else if (SimpleGems.getInstance().getDataType() == DataType.SQLITE) {
            try {
                return SimpleGems.getInstance().getSqLiteManager().getPlayerGems(player.getUniqueId()).getGems();
            } catch (SQLException exception) {
                Color.log("&cSQLite Error: " + exception.getMessage());
            }
        } else if (SimpleGems.getInstance().getDataType() == DataType.FLAT_FILE) {
            return SimpleGems.getInstance().getPlayerMapper().getGems(player.getUniqueId());
        }
        return 0;
    }

    public boolean hasOfflineGems(OfflinePlayer player, long amount) {
        return getOfflineGems(player) >= amount;
    }

    public void payGems(Player player, Player target, long amount, boolean silent) {
        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        if (player == target) {
            Color.sendCustomMessage(player, "&cYou can't pay yourself.");
            return;
        }

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .add("player", target.getName())
                .add("gems", String.valueOf(amount))
                .add("gems_formatted", Methods.format(amount))
                .add("gems_decimal", Methods.formatDecimal(amount))
                .build();

        if (profile.getGems().hasAmount(amount)) {
            SimpleGems.getInstance().getGemsAPI().takeGems(player, amount);
            SimpleGems.getInstance().getGemsAPI().giveGems(target, amount);

            Color.sendMessage(player, "gems-paid", placeholders);
            if (silent) return;
            Color.sendMessage(target, "gems-received", placeholders);
        } else {
            Color.sendMessage(player, "not-enough-pay", placeholders);
        }
    }

    public void payOfflineGems(Player player, OfflinePlayer target, long amount) {
        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .add("player", target.getName())
                .add("gems", String.valueOf(amount))
                .add("gems_formatted", Methods.format(amount))
                .add("gems_decimal", Methods.formatDecimal(amount))
                .build();

        if (profile.getGems().hasAmount(amount)) {
            SimpleGems.getInstance().getGemsAPI().takeGems(player, amount);
            SimpleGems.getInstance().getGemsAPI().giveOfflineGems(target, amount);

            Color.sendMessage(player, "gems-paid", placeholders);
        } else {
            Color.sendMessage(player, "not-enough-pay", placeholders);
        }
    }

    public void withdrawGems(Player player, long amount) {
        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .add("gems", String.valueOf(amount))
                .add("gems_formatted", Methods.format(amount))
                .add("gems_decimal", Methods.formatDecimal(amount))
                .build();

        if (SimpleGems.getInstance().getGemsAPI().hasGems(player, amount)) {
            SimpleGems.getInstance().getGemsAPI().takeGems(player, amount);
            SimpleGems.getInstance().getGemsAPI().giveGemsItem(player, amount);

            Color.sendMessage(player, "gems-withdrawn", placeholders);
        } else {
            Color.sendMessage(player, "not-enough-withdraw", placeholders);
        }
    }

    public void giveGemsItem(Player player, long amount) {
        ItemStack gemsItem = getGemsItem(UUID.randomUUID(), amount);

        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(gemsItem);
            player.updateInventory();
        } else {
            player.getWorld().dropItem(player.getLocation(), gemsItem);
        }
    }

    public ItemStack getGemsItem(UUID uuid, long amount) {
        String name = SimpleGems.getInstance().getSettings().GEMS_ITEM_NAME;
        XMaterial material = Utilities.getMaterial(SimpleGems.getInstance().getSettings().GEMS_ITEM_MATERIAL);
        int data = SimpleGems.getInstance().getSettings().GEMS_ITEM_DATA;
        List<String> lore = SimpleGems.getInstance().getSettings().GEMS_ITEM_LORE;
        ItemBuilder item = new ItemBuilder(material.parseMaterial(), 1);

        if (SimpleGems.getInstance().getSettings().GEMS_ITEM_GLOW) {
            item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
            ItemMeta itemMeta = item.toItemStack().getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.toItemStack().setItemMeta(itemMeta);
        }

        if (SimpleGems.getInstance().getSettings().GEMS_ITEM_CUSTOM_DATA) {
            if (ReflectionUtils.MINOR_NUMBER >= 14) {
                item.setCustomModelData(SimpleGems.getInstance().getSettings().GEMS_ITEM_CUSTOM_MODEL_DATA);
            } else {
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
            }
        }

        // Attempt to insert a NBT tag of the gems value instead of filling the inventory

        NBTItem nbtItem = new NBTItem(item.toItemStack());
        nbtItem.setUUID("uuid", uuid);
        nbtItem.setLong("gems-item-value", amount);
        nbtItem.applyNBT(item.toItemStack());

        if (nbtItem.hasTag("gems-item-value")) {
            long foundValue = nbtItem.getLong("gems-item-value");

            item.setName(name.replace("%value%", String.valueOf(foundValue))
                    .replace("%gems%", String.valueOf(foundValue)));
            lore.forEach(s -> item.addLoreLine(Color.translate(s
                    .replace("%value%", String.valueOf(foundValue))
                    .replace("%gems%", String.valueOf(foundValue))
            )));
        }

        item.setDurability(data);

        return item.toItemStack();
    }

    public String formatDecimal(long amount) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(amount);
    }

    public String format(long amount) {
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