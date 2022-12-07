package me.refracdevelopment.simplegems;

import me.refracdevelopment.simplegems.manager.data.ProfileData;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SimpleGemsAPI {

    /**
     * The SimpleGemsAPI allows you to hook into SimpleGems to either modify and grab data
     * or to add new features and events.
     */
    public static SimpleGems plugin = SimpleGems.getInstance();
    public static SimpleGemsAPI INSTANCE;

    public SimpleGemsAPI() {
        INSTANCE = this;
        Color.log("&aSimpleGemsAPI has been enabled!");
        Color.log("&aWiki & Download: https://simplegems.refracdev.ml");
    }

    /**
     * @return Is the SimpleGemsAPI enabled and registered?
     */
    public static boolean isRegistered() {
        return INSTANCE != null;
    }

    /**
     * Used to get a player's profile data.
     *
     * @return Player's profile data
     */
    public ProfileData getProfileData(Player player) {
        return plugin.getProfileManager().getProfile(player.getUniqueId()).getData();
    }

    /**
     * Used to get player gems.
     *
     * @param player data
     * @return Player's gems
     */
    public double getGems(Player player) {
        if (getProfileData(player) == null) {
            return 0;
        }

        return getProfileData(player).getGems().getStat();
    }

    /**
     * Used to get offline player gems.
     *
     * @param player data
     * @return Player's gems
     */
    public double getOfflineGems(OfflinePlayer player) {
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
    public void giveGemsItem(Player player, int amount) {
        if (getProfileData(player) == null) return;

        Methods.giveGemsItem(player, amount);
    }

    /**
     * This will give gems in item form
     * to the player who withdraw them or
     * received them by doing events etc
     *
     * @return an item stack to redeem gems
     */
    public ItemStack getGemsItem() {
        return Methods.getGemsItem();
    }

    /**
     * Used to check if the player has enough gems
     *
     * @param player player
     * @param amount gems
     * @return If the player has enough gems
     */
    public boolean hasGems(Player player, double amount) {
        if (getProfileData(player) == null) return false;

        return getProfileData(player).getGems().hasStat(amount);
    }

    /**
     * Used to check if the offline player has enough gems
     *
     * @param player player
     * @param amount gems
     * @return If the player has enough gems
     */
    public boolean hasGems(OfflinePlayer player, double amount) {
        return Methods.hasOfflineGems(player, amount);
    }

    /**
     * Used to give player gems.
     *
     * @param player player
     * @param amount gems
     */
    public void giveGems(Player player, double amount) {
        if (getProfileData(player) == null) return;

        getProfileData(player).getGems().incrementStat(amount);
    }

    /**
     * Used to give offline player gems.
     *
     * @param player player
     * @param amount gems
     */
    public void giveOfflineGems(OfflinePlayer player, double amount) {
        Methods.giveOfflineGems(player, amount);
    }

    /**
     * Used to take player gems.
     *
     * @param player player
     * @param amount gems
     */
    public void takeGems(Player player, double amount) {
        if (getProfileData(player) == null) return;

        getProfileData(player).getGems().decrementStat(amount);
    }

    /**
     * Used to take offline player gems.
     *
     * @param player player
     * @param amount gems
     */
    public void takeOfflineGems(OfflinePlayer player, double amount) {
        Methods.takeOfflineGems(player, amount);
    }

    /**
     * Used to set player gems.
     *
     * @param player player
     * @param amount gems
     */
    public void setGems(Player player, double amount) {
        if (getProfileData(player) == null) return;

        getProfileData(player).getGems().setStat(amount);
    }

    /**
     * Used to set offline player gems.
     *
     * @param player player
     * @param amount gems
     */
    public void setOfflineGems(OfflinePlayer player, double amount) {
        Methods.setOfflineGems(player, amount);
    }
}