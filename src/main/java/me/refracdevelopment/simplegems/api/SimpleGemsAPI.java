package me.refracdevelopment.simplegems.api;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.api.events.impl.GemsAddEvent;
import me.refracdevelopment.simplegems.api.events.impl.GemsPayEvent;
import me.refracdevelopment.simplegems.api.events.impl.GemsRemoveEvent;
import me.refracdevelopment.simplegems.api.events.impl.GemsSetEvent;
import me.refracdevelopment.simplegems.player.data.ProfileData;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The SimpleGemsAPI allows you to hook into SimpleGems to either modify and grab data
 * or to add new features and events.
 */
public class SimpleGemsAPI {

    public static SimpleGems plugin = SimpleGems.getInstance();
    public static SimpleGemsAPI INSTANCE;

    public SimpleGemsAPI() {
        INSTANCE = this;
        Color.log("&eSimpleGemsAPI has been enabled!");
        Color.log("&eWiki: https://refracdevelopment.gitbook.io/");
    }

    /**
     * @return Is the SimpleGemsAPI enabled and registered?
     */
    public static boolean isRegistered() {
        return INSTANCE != null;
    }

    /**
     * Used to get a cached online player's profile data.
     *
     * @return online player's cached profile data
     */
    public ProfileData getProfileData(Player player) {
        return plugin.getProfileManager().getProfile(player.getUniqueId()).getData();
    }

    /**
     * Used to get player gems.
     *
     * @param player online player
     * @return online player's gems amount
     */
    public long getGems(Player player) {
        if (getProfileData(player) == null) {
            return 0;
        }

        return getProfileData(player).getGems().getAmount();
    }

    /**
     * Used to get offline player gems.
     *
     * @param player offline player
     * @return offline player's gems amount
     */
    public long getOfflineGems(OfflinePlayer player) {
        return Methods.getOfflineGems(player);
    }

    /**
     * This will give gems in item form
     * to the player who withdraw them or
     * received them by doing events etc
     *
     * @param player player profile
     * @param amount gems to remove and turn into an item
     */
    public void giveGemsItem(Player player, long amount) {
        if (getProfileData(player) == null) return;

        Methods.giveGemsItem(player, amount);
    }

    /**
     * This is used to compare gems
     * that are in item form to deposit properly
     *
     * @param amount The amount of the item.
     * @return an item stack to redeem gems
     */
    public ItemStack getGemsItem(long amount) {
        return Methods.getGemsItem(amount);
    }

    /**
     * Used to check if the player has enough gems
     *
     * @param player online player
     * @param amount amount of gems to check
     * @return If the player has enough gems
     */
    public boolean hasGems(Player player, long amount) {
        if (getProfileData(player) == null) return false;

        return getProfileData(player).getGems().hasAmount(amount);
    }

    /**
     * Used to check if the offline player has enough gems
     *
     * @param player offline player
     * @param amount amount of gems to check
     * @return If the player has enough gems
     */
    public boolean hasOfflineGems(OfflinePlayer player, long amount) {
        return Methods.hasOfflineGems(player, amount);
    }

    /**
     * Used to give player gems.
     *
     * @param player online player
     * @param amount amount of gems to give
     */
    public void giveGems(Player player, long amount) {
        if (getProfileData(player) == null) return;

        GemsAddEvent event = new GemsAddEvent(player, amount);
        Bukkit.getServer().getPluginManager().callEvent(event);
        getProfileData(player).getGems().incrementAmount(amount);
        Tasks.runAsync(plugin, () -> getProfileData(player).save());
    }

    /**
     * Used to give offline player gems.
     *
     * @param player offline player
     * @param amount amount of gems to give
     */
    public void giveOfflineGems(OfflinePlayer player, long amount) {
        Methods.giveOfflineGems(player, amount);
    }

    /**
     * Used to take player gems.
     *
     * @param player online player
     * @param amount amount of gems to take
     */
    public void takeGems(Player player, long amount) {
        if (getProfileData(player) == null) return;

        GemsRemoveEvent event = new GemsRemoveEvent(player, amount);
        Bukkit.getServer().getPluginManager().callEvent(event);
        getProfileData(player).getGems().decrementAmount(amount);
        Tasks.runAsync(plugin, () -> getProfileData(player).save());
    }

    /**
     * Used to take offline player gems.
     *
     * @param player offline player
     * @param amount amount of gems to take
     */
    public void takeOfflineGems(OfflinePlayer player, long amount) {
        Methods.takeOfflineGems(player, amount);
    }

    /**
     * Used to set player gems.
     *
     * @param player online player
     * @param amount amount of gems to set
     */
    public void setGems(Player player, long amount) {
        if (getProfileData(player) == null) return;

        GemsSetEvent event = new GemsSetEvent(player, amount);
        Bukkit.getServer().getPluginManager().callEvent(event);
        getProfileData(player).getGems().setAmount(amount);
        Tasks.runAsync(plugin, () -> getProfileData(player).save());
    }

    /**
     * Used to set offline player gems.
     *
     * @param player offline player
     * @param amount amount of gems to set
     */
    public void setOfflineGems(OfflinePlayer player, long amount) {
        Methods.setOfflineGems(player, amount);
    }

    /**
     * Used to pay player gems
     *
     * @param player online player
     * @param target online target
     * @param amount amount of gems to pay
     */
    public void payGems(Player player, Player target, long amount, boolean silent) {
        GemsPayEvent event = new GemsPayEvent(player, target, amount);
        Bukkit.getServer().getPluginManager().callEvent(event);
        Methods.payGems(player, target, amount, silent);
    }

    /**
     * Used to pay offline player gems
     *
     * @param player online player
     * @param target offline target
     * @param amount amount of gems to pay
     */
    public void payOfflineGems(Player player, OfflinePlayer target, long amount) {
        Methods.payOfflineGems(player, target, amount);
    }

    /**
     * Used to withdraw player gems.
     *
     * @param player online player
     * @param amount amount of gems to withdraw
     */
    public void withdrawGems(Player player, long amount) {
        GemsRemoveEvent event = new GemsRemoveEvent(player, amount);
        Bukkit.getServer().getPluginManager().callEvent(event);
        Methods.withdrawGems(player, amount);
    }
}