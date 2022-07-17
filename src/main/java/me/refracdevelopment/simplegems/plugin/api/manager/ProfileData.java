/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 RefracDevelopment
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.refracdevelopment.simplegems.plugin.api.manager;

import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.utilities.Methods;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2022-7-14
 */
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