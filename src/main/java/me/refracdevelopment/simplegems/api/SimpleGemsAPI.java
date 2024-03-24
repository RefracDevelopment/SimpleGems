package me.refracdevelopment.simplegems.api;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.api.events.impl.*;
import me.refracdevelopment.simplegems.player.data.ProfileData;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * The SimpleGemsAPI allows you to hook into SimpleGems to either modify and grab data
 * or to add new features and events.
 */
public class SimpleGemsAPI {

    public SimpleGemsAPI() {
        Color.log("&aSimpleGemsAPI has been enabled!");
        Color.log("&aWiki: &ehttps://refracdevelopment.gitbook.io/simplegems/");
    }

    /**
     * @return Is the SimpleGemsAPI enabled and registered?
     */
    public boolean isRegistered() {
        return SimpleGems.getInstance() != null;
    }

    /**
     * Used to get a cached online player's profile data.
     *
     * @return online player's cached profile data
     */
    public ProfileData getProfileData(Player player) {
        return SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();
    }

    /**
     * Used to get player gems.
     *
     * @param player online player
     * @return online player's gems amount
     */
    public long getGems(Player player) {
        if (getProfileData(player) == null)
            return 0;

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
        if (getProfileData(player) == null)
            return;

        Methods.giveGemsItem(player, amount);
    }

    /**
     * This is used to compare gems
     * that are in item form to deposit properly
     *
     * @param amount The amount of the item.
     * @return an item stack to redeem gems
     */
    public ItemStack getGemsItem(UUID uuid, long amount) {
        return Methods.getGemsItem(uuid, amount);
    }

    /**
     * Used to check if the player has enough gems
     *
     * @param player online player
     * @param amount amount of gems to check
     * @return If the player has enough gems
     */
    public boolean hasGems(Player player, long amount) {
        if (getProfileData(player) == null)
            return false;

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
        if (getProfileData(player) == null)
            return;

        GemsAddEvent event = new GemsAddEvent(player, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        getProfileData(player).getGems().incrementAmount(amount);
        Tasks.runAsync(() -> getProfileData(player).save(player));
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
        if (getProfileData(player) == null)
            return;

        GemsRemoveEvent event = new GemsRemoveEvent(player, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        getProfileData(player).getGems().decrementAmount(amount);
        Tasks.runAsync(() -> getProfileData(player).save(player));
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
     * @param target online player
     * @param amount amount of gems to set
     */
    public void setGems(Player target, long amount) {
        if (getProfileData(target) == null)
            return;

        GemsSetEvent event = new GemsSetEvent(target, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        getProfileData(target).getGems().setAmount(amount);
        Tasks.runAsync(() -> getProfileData(target).save(target));
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
        if (getProfileData(player) == null)
            return;

        GemsPayEvent event = new GemsPayEvent(player, target, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

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
        if (getProfileData(player) == null)
            return;

        Methods.payOfflineGems(player, target, amount);
    }

    /**
     * Used to withdraw player gems.
     *
     * @param player online player
     * @param amount amount of gems to withdraw
     */
    public void withdrawGems(Player player, long amount) {
        if (getProfileData(player) == null)
            return;

        GemsWithdrawEvent event = new GemsWithdrawEvent(player, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        Methods.withdrawGems(player, amount);
    }
}