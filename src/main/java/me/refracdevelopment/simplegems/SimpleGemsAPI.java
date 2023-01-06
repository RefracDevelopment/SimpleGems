package me.refracdevelopment.simplegems;

import me.refracdevelopment.simplegems.data.ProfileData;
import me.refracdevelopment.simplegems.utilities.Methods;
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
     * Used to get a cached player's profile data.
     *
     * @return Player's cached profile data
     */
    public ProfileData getProfileData(Player player) {
        return plugin.getProfileManager().getProfile(player.getUniqueId()).getData();
    }

    /**
     * Used to get player gems.
     *
     * @param player player
     * @return player's gems amount
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
    public long getGems(OfflinePlayer player) {
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
    public boolean hasGems(OfflinePlayer player, long amount) {
        return Methods.hasOfflineGems(player, amount);
    }

    /**
     * Used to give player gems.
     *
     * @param player offline player
     * @param amount amount of gems to give
     */
    public void giveGems(Player player, long amount) {
        if (getProfileData(player) == null) return;

        getProfileData(player).getGems().incrementAmount(amount);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> getProfileData(player).save(player));
    }

    /**
     * Used to give offline player gems.
     *
     * @param player offline player
     * @param amount amount of gems to give
     */
    public void giveGems(OfflinePlayer player, long amount) {
        Methods.giveOfflineGems(player, amount);
    }

    /**
     * Used to take player gems.
     *
     * @param player offline player
     * @param amount amount of gems to take
     */
    public void takeGems(Player player, long amount) {
        if (getProfileData(player) == null) return;

        getProfileData(player).getGems().decrementAmount(amount);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> getProfileData(player).save(player));
    }

    /**
     * Used to take offline player gems.
     *
     * @param player offline player
     * @param amount gems
     */
    public void takeGems(OfflinePlayer player, long amount) {
        Methods.takeOfflineGems(player, amount);
    }

    /**
     * Used to set player gems.
     *
     * @param player player
     * @param amount gems
     */
    public void setGems(Player player, long amount) {
        if (getProfileData(player) == null) return;

        getProfileData(player).getGems().setAmount(amount);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> getProfileData(player).save(player));
    }

    /**
     * Used to set offline player gems.
     *
     * @param player offline player
     * @param amount gems
     */
    public void setGems(OfflinePlayer player, long amount) {
        Methods.setOfflineGems(player, amount);
    }
}