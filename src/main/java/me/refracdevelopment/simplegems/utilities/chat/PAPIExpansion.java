package me.refracdevelopment.simplegems.utilities.chat;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.utilities.Methods;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return SimpleGems.getInstance().getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "simplegems";
    }

    @Override
    public @NotNull String getVersion() {
        return SimpleGems.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        double gems = SimpleGemsAPI.INSTANCE.getGems(player);

        if (params.equalsIgnoreCase("balance") || params.equalsIgnoreCase("gems")) {
            return String.valueOf(gems);
        }
        if (params.equalsIgnoreCase("balance_formatted") || params.equalsIgnoreCase("gems_formatted")) {
            return Methods.format(gems);
        }
        if (params.equalsIgnoreCase("balance_decimal") || params.equalsIgnoreCase("gems_decimal")) {
            return Methods.formatDecimal(gems);
        }

        return null;
    }
}