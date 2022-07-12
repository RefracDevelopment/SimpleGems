package me.refracdevelopment.simplegems.plugin.utilities.chat;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.refracdevelopment.simplegems.plugin.utilities.Methods;
import me.refracdevelopment.simplegems.plugin.utilities.Settings;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class PlaceHolderExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return Settings.getDeveloper;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "simplegems";
    }

    @Override
    public @NotNull String getVersion() {
        return Settings.getVersion;
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("balance")) {
            return Methods.format(Methods.getGems(player));
        }
        if (params.equalsIgnoreCase("balance_decimal")) {
            return Methods.formatDec(Methods.getGems(player));
        }
        return null;
    }
}