package me.refracdevelopment.simplegems.utilities.chat;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.data.Profile;
import me.refracdevelopment.simplegems.utilities.Methods;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceHolderExpansion extends PlaceholderExpansion {

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
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (params.equalsIgnoreCase("balance")) {
            return Methods.format(profile.getData().getGems().getStat());
        }
        if (params.equalsIgnoreCase("balance_formatted")) {
            return Methods.formatDec(profile.getData().getGems().getStat());
        }

        return null;
    }
}