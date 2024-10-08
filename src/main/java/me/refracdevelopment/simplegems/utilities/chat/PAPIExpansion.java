package me.refracdevelopment.simplegems.utilities.chat;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Methods;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PAPIExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return SimpleGems.getInstance().getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getIdentifier() {
        return SimpleGems.getInstance().getDescription().getName();
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
        double gems = SimpleGems.getInstance().getGemsAPI().getGems(player);

        switch (params) {
            case "balance":
                return String.valueOf(gems);
            case "balance_formatted":
                return Methods.format(gems);
            case "balance_decimal":
                return Methods.formatDecimal(gems);
            default:
                if (params.startsWith("player_")) {
                    try {
                        int value = Integer.parseInt(params.replace("player_", "")) - 1;
                        Map.Entry<String, Double> leaderboardMap = SimpleGems.getInstance().getLeaderboardManager().getCachedMap().entrySet().stream().toList().get(value);
                        String name = leaderboardMap.getKey();
                        gems = leaderboardMap.getValue();

                        return SimpleGems.getInstance().getSettings().GEMS_TOP_FORMAT
                                .replace("%number%", String.valueOf(value + 1))
                                .replace("%player%", name)
                                .replace("%gems%", String.valueOf(gems))
                                .replace("%gems_formatted%", Methods.format(gems))
                                .replace("%gems_decimal%", Methods.formatDecimal(gems));
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        return "Error...";
                    }
                }

                return String.valueOf(gems);
        }
    }
}