package me.refracdevelopment.simplegems.plugin.api.manager;

import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.utilities.Methods;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ProfileData {

    /**
     * Used to get a player's profile data.
     *
     * @return Player's profile data
     */
    public me.refracdevelopment.simplegems.plugin.manager.ProfileData getProfileData(Player player) {
        return SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();
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

        return Methods.getGems(player);
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

        return Methods.hasGems(player, amount);
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

        Methods.giveGems(player, amount);
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

        Methods.takeGems(player, amount);
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

        Methods.setGems(player, amount);
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