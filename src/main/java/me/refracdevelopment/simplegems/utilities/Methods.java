package me.refracdevelopment.simplegems.utilities;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemFlag;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBT;
import dev.lone.itemsadder.api.CustomStack;
import lombok.experimental.UtilityClass;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.player.data.ProfileData;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.chat.StringPlaceholders;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@UtilityClass
public class Methods {

    public void setOfflineGems(OfflinePlayer player, double amount) {
        ProfileData profile = SimpleGems.getInstance().getGemsAPI().getProfileData(player);

        if (profile == null)
            return;

        profile.getGems().setAmount(amount);

        Tasks.runAsync(profile::save);
    }

    public void giveOfflineGems(OfflinePlayer player, double amount) {
        ProfileData profile = SimpleGems.getInstance().getGemsAPI().getProfileData(player);

        if (profile == null)
            return;

        profile.getGems().incrementAmount(amount);

        Tasks.runAsync(profile::save);
    }

    public void takeOfflineGems(OfflinePlayer player, double amount) {
        ProfileData profile = SimpleGems.getInstance().getGemsAPI().getProfileData(player);

        if (profile == null)
            return;

        profile.getGems().decrementAmount(amount);

        Tasks.runAsync(profile::save);
    }

    public double getOfflineGems(OfflinePlayer player) {
        ProfileData profile = SimpleGems.getInstance().getGemsAPI().getProfileData(player);

        if (profile == null)
            return 0.0;

        return profile.getGems().getAmount();
    }

    public boolean hasOfflineGems(OfflinePlayer player, double amount) {
        return getOfflineGems(player) >= amount;
    }

    public void payGems(Player player, Player target, double amount, boolean silent) {
        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();
        ProfileData targetProfile = SimpleGems.getInstance().getProfileManager().getProfile(target.getUniqueId()).getData();

        if (player == target) {
            RyMessageUtils.sendPluginMessage(player, "cant-pay-yourself");
            return;
        }

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(target))
                .add("gems", String.valueOf(amount))
                .add("gems_formatted", format(amount))
                .add("gems_decimal", formatDecimal(amount))
                .build();

        if (!profile.getGems().hasAmount(amount)) {
            RyMessageUtils.sendPluginMessage(player, "not-enough-pay", placeholders);
            return;
        }

        profile.getGems().decrementAmount(amount);
        targetProfile.getGems().incrementAmount(amount);

        Tasks.runAsync(profile::save);
        Tasks.runAsync(targetProfile::save);

        RyMessageUtils.sendPluginMessage(player, "gems-paid", placeholders);

        if (silent)
            return;

        RyMessageUtils.sendPluginMessage(target, "gems-received", placeholders);
    }

    public void payOfflineGems(Player player, OfflinePlayer target, double amount) {
        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setOfflinePlaceholders(target))
                .add("player", target.getName())
                .add("gems", String.valueOf(amount))
                .add("gems_formatted", format(amount))
                .add("gems_decimal", formatDecimal(amount))
                .build();

        if (!profile.getGems().hasAmount(amount)) {
            RyMessageUtils.sendPluginMessage(player, "not-enough-pay", placeholders);
            return;
        }

        profile.getGems().decrementAmount(amount);
        giveOfflineGems(target, amount);

        Tasks.runAsync(profile::save);

        RyMessageUtils.sendPluginMessage(player, "gems-paid", placeholders);
    }

    public void withdrawGems(Player player, double amount) {
        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .add("gems", String.valueOf(amount))
                .add("gems_formatted", Methods.format(amount))
                .add("gems_decimal", Methods.formatDecimal(amount))
                .build();

        if (!profile.getGems().hasAmount(amount)) {
            RyMessageUtils.sendPluginMessage(player, "not-enough-withdraw", placeholders);
            return;
        }

        profile.getGems().decrementAmount(amount);
        giveGemsItem(player, amount);

        Tasks.runAsync(profile::save);

        RyMessageUtils.sendPluginMessage(player, "gems-withdrawn", placeholders);
    }

    public void giveGemsItem(Player player, double amount) {
        ItemStack gemsItem = getGemsItem(player, amount);

        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), gemsItem);
            return;
        }

        player.getInventory().addItem(gemsItem);
    }

    public ItemStack getGemsItem(Player player, double amount) {
        String name = SimpleGems.getInstance().getSettings().GEMS_ITEM_NAME;
        String material = SimpleGems.getInstance().getSettings().GEMS_ITEM_MATERIAL;
        int durability = SimpleGems.getInstance().getSettings().GEMS_ITEM_DURABILITY;
        List<String> lore = SimpleGems.getInstance().getSettings().GEMS_ITEM_LORE;
        ItemBuilder item = new ItemBuilder(getMaterial(material).get(), 1);

        if (SimpleGems.getInstance().getSettings().GEMS_ITEM_GLOW) {
            item.addEnchant(XEnchantment.POWER.get(), 1);
            item.setItemFlags(XItemFlag.HIDE_ENCHANTS.get());
        }

        if (SimpleGems.getInstance().getSettings().GEMS_ITEM_CUSTOM_DATA) {
            item.setCustomModelData(SimpleGems.getInstance().getSettings().GEMS_ITEM_CUSTOM_MODEL_DATA);
        }

        if (SimpleGems.getInstance().getSettings().GEMS_ITEM_ITEMS_ADDER) {
            CustomStack api = CustomStack.getInstance(material);

            if (api != null)
                item = new ItemBuilder(api.getItemStack());
            else
                RyMessageUtils.sendConsole(true, "&cAn error occurred when trying to set an items adder custom item. Make sure you are typing the correct namespaced id.");
        }

        // Attempt to insert a NBT tag of the gems value instead of filling the inventory

        item.setDurability(durability);

        NBT.modify(item.toItemStack(), nbt -> {
            nbt.setDouble("gems-item-value", amount);
        });

        ItemBuilder finalItem = item;

        double foundValue = NBT.get(finalItem.toItemStack(), nbt -> (Double) nbt.getDouble("gems-item-value"));

        finalItem.setName(RyMessageUtils.translate(player, name
                .replace("%value%", String.valueOf(foundValue))
                .replace("%gems%", String.valueOf(foundValue))
                .replace("%gems_formatted%", format(foundValue))
                .replace("%gems_decimal%", formatDecimal(foundValue))
        ));

        lore.forEach(line -> finalItem.addLoreLine(RyMessageUtils.translate(player, line
                .replace("%value%", String.valueOf(foundValue))
                .replace("%gems%", String.valueOf(foundValue))
                .replace("%gems_formatted%", format(foundValue))
                .replace("%gems_decimal%", formatDecimal(foundValue))
        )));

        return finalItem.toItemStack();
    }

    public String formatDecimal(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        decimalFormat.setMaximumFractionDigits(340);

        return decimalFormat.format(amount);
    }

    public String format(double amount) {
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

    public XMaterial getMaterial(String source) {
        XMaterial material;
        try {
            material = XMaterial.matchXMaterial(source).get();
        } catch (Exception e) {
            material = XMaterial.REDSTONE_BLOCK;
        }
        return material;
    }
}