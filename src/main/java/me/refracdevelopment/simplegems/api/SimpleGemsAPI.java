package me.refracdevelopment.simplegems.api;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.api.events.impl.*;
import me.refracdevelopment.simplegems.player.data.ProfileData;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The SimpleGemsAPI allows you to hook into SimpleGems to either modify and grab data
 * or to add new features and events.
 */
public class SimpleGemsAPI {

    public SimpleGemsAPI() {
        RyMessageUtils.sendConsole(true, "&aSimpleGemsAPI has been enabled!");
        RyMessageUtils.sendConsole(true, "&aWiki: &ehttps://refracdevelopment.gitbook.io/simplegems/");
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
    public double getGems(Player player) {
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
    public double getOfflineGems(OfflinePlayer player) {
        return Methods.getOfflineGems(player);
    }

    /**
     * This will give gems in item form
     * to the player who withdrew them or
     * received them by doing events etc.
     *
     * @param player player profile
     * @param amount gems to remove and turn into an item
     */
    public void giveGemsItem(Player player, double amount) {
        if (getProfileData(player) == null)
            return;

        Methods.giveGemsItem(player, amount);
    }

    /**
     * This is used to compare gems
     * that are in item form to deposit properly.
     *
     * @param player The player
     * @param amount The amount of the item.
     * @return an item stack to redeem gems
     */
    public ItemStack getGemsItem(Player player, double amount) {
        return Methods.getGemsItem(player, amount);
    }

    /**
     * Used to check if the player has enough gems.
     *
     * @param player online player
     * @param amount amount of gems to check
     * @return true - If the player has enough gems
     */
    public boolean hasGems(Player player, double amount) {
        if (getProfileData(player) == null)
            return false;

        return getProfileData(player).getGems().hasAmount(amount);
    }

    /**
     * Used to check if the offline player has enough gems.
     *
     * @param player offline player
     * @param amount amount of gems to check
     * @return true - If the player has enough gems
     */
    public boolean hasOfflineGems(OfflinePlayer player, double amount) {
        return Methods.hasOfflineGems(player, amount);
    }

    /**
     * Used to give/deposit player gems into their account.
     *
     * @param target the player who received the gems
     * @param amount amount of gems to give
     */
    public void giveGems(Player target, double amount) {
        if (getProfileData(target) == null)
            return;

        GemsAddEvent event = new GemsAddEvent(null, target, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        getProfileData(target).getGems().incrementAmount(amount);
        Tasks.runAsync(() -> getProfileData(target).save(target));
    }

    /**
     * Used to give/deposit player gems into their account.
     *
     * @param player the player who gave the gems
     * @param target the player who received the gems
     * @param amount amount of gems to give
     */
    public void giveGems(Player player, Player target, double amount) {
        if (getProfileData(target) == null)
            return;

        GemsAddEvent event = new GemsAddEvent(player, target, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        getProfileData(target).getGems().incrementAmount(amount);
        Tasks.runAsync(() -> getProfileData(target).save(target));
    }

    /**
     * Used to give/deposit offline player gems into their account.
     *
     * @param player the target player
     * @param amount amount of gems to give
     */
    public void giveOfflineGems(OfflinePlayer player, double amount) {
        Methods.giveOfflineGems(player, amount);
    }

    /**
     * Used to take/remove player gems from their account.
     *
     * @param target the target player
     * @param amount amount of gems to take
     */
    public void takeGems(Player target, double amount) {
        if (getProfileData(target) == null)
            return;

        GemsRemoveEvent event = new GemsRemoveEvent(null, target, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        getProfileData(target).getGems().decrementAmount(amount);
        Tasks.runAsync(() -> getProfileData(target).save(target));
    }

    /**
     * Used to take/remove player gems from their account.
     *
     * @param player the player who took the gems
     * @param target the target player
     * @param amount amount of gems to take
     */
    public void takeGems(Player player, Player target, double amount) {
        if (getProfileData(target) == null)
            return;

        GemsRemoveEvent event = new GemsRemoveEvent(player, target, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        getProfileData(target).getGems().decrementAmount(amount);
        Tasks.runAsync(() -> getProfileData(target).save(target));
    }

    /**
     * Used to take offline player gems.
     *
     * @param target the target player
     * @param amount amount of gems to take
     */
    public void takeOfflineGems(OfflinePlayer target, double amount) {
        Methods.takeOfflineGems(target, amount);
    }

    /**
     * Used to set a player gems.
     *
     * @param target the player who received the gems
     * @param amount amount of gems to set
     */
    public void setGems(Player target, double amount) {
        if (getProfileData(target) == null)
            return;

        GemsSetEvent event = new GemsSetEvent(null, target, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        getProfileData(target).getGems().setAmount(amount);
        Tasks.runAsync(() -> getProfileData(target).save(target));
    }

    /**
     * Used to set a player gems.
     *
     * @param player the player who set the gems
     * @param target the player who received the gems
     * @param amount amount of gems to set
     */
    public void setGems(Player player, Player target, double amount) {
        if (getProfileData(target) == null)
            return;

        GemsSetEvent event = new GemsSetEvent(player, target, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        getProfileData(target).getGems().setAmount(amount);
        Tasks.runAsync(() -> getProfileData(target).save(target));
    }

    /**
     * Used to set an offline player gems.
     *
     * @param player the player who received the gems
     * @param amount amount of gems to set
     */
    public void setOfflineGems(OfflinePlayer player, double amount) {
        Methods.setOfflineGems(player, amount);
    }

    /**
     * Used to pay the target player gems
     * from the player's account.
     *
     * @param player the player who paid the gems
     * @param target the player who received the gems
     * @param amount amount of gems to pay
     */
    public void payGems(Player player, Player target, double amount, boolean silent) {
        if (getProfileData(player) == null)
            return;

        GemsPayEvent event = new GemsPayEvent(player, target, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        Methods.payGems(player, target, amount, silent);
    }

    /**
     * Used to pay the target offline player gems
     * from the player's account.
     *
     * @param player the player who paid the gems
     * @param target the player who received the gems
     * @param amount amount of gems to pay
     */
    public void payOfflineGems(Player player, OfflinePlayer target, double amount) {
        if (getProfileData(player) == null)
            return;

        Methods.payOfflineGems(player, target, amount);
    }

    /**
     * Used to withdraw player gems into an item form.
     * <p>
     * WARNING: If you want to take/remove gems from
     * another player's account then you should use #takeGems instead.
     *
     * @param player the player who received the gems in item form
     * @param amount amount of gems to withdraw
     */
    public void withdrawGems(Player player, double amount) {
        if (getProfileData(player) == null)
            return;

        GemsWithdrawEvent event = new GemsWithdrawEvent(player, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        Methods.withdrawGems(player, amount);
    }

    /**
     * Used to deposit a gems item into the player's account.
     * <p>
     * WARNING: If you want to add/give gems to
     * another player's account then you should use #giveGems instead.
     *
     * @param player the player who will receive the gems
     * @param amount amount of gems to deposit
     */
    public void depositGems(Player player, double amount) {
        if (getProfileData(player) == null)
            return;

        GemsDepositEvent event = new GemsDepositEvent(player, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        getProfileData(player).getGems().incrementAmount(amount);
        Tasks.runAsync(() -> getProfileData(player).save(player));
    }

}