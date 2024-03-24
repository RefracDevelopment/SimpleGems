package me.refracdevelopment.simplegems.utilities.chat;

import lombok.experimental.UtilityClass;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Methods;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@UtilityClass
public class Placeholders {

    public String setPlaceholders(CommandSender sender, String placeholder) {
        placeholder = placeholder.replace("%prefix%", SimpleGems.getInstance().getLocaleFile().getString("prefix"));

        if (sender instanceof Player) {
            Player player = (Player) sender;
            long gems = SimpleGems.getInstance().getGemsAPI().getGems(player);

            placeholder = placeholder.replace("%player%", player.getName());
            placeholder = placeholder.replace("%gems%", String.valueOf(gems));
            placeholder = placeholder.replace("%gems_formatted%", Methods.format(gems));
            placeholder = placeholder.replace("%gems_decimal%", Methods.formatDecimal(gems));
            placeholder = placeholder.replace("%displayname%", player.getDisplayName());
        }

        placeholder = placeholder.replace("%arrow%", "»");
        placeholder = placeholder.replace("%arrowright%", "»");
        placeholder = placeholder.replace("%arrowleft%", "«");
        placeholder = placeholder.replace("%star%", "✦");
        placeholder = placeholder.replace("%circle%", "∙");
        placeholder = placeholder.replace("|", "⎟");

        return placeholder;
    }

    public StringPlaceholders setPlaceholders(CommandSender sender) {
        StringPlaceholders.Builder placeholders = StringPlaceholders.builder();

        placeholders.add("prefix", SimpleGems.getInstance().getLocaleFile().getString("prefix"));

        if (sender instanceof Player) {
            Player player = (Player) sender;
            long gems = SimpleGems.getInstance().getGemsAPI().getGems(player);

            placeholders.add("player", player.getName());
            placeholders.add("gems", String.valueOf(gems));
            placeholders.add("gems_formatted", Methods.format(gems));
            placeholders.add("gems_decimal", Methods.formatDecimal(gems));
            placeholders.add("displayname", player.getDisplayName());
        }

        placeholders.add("arrow", "»");
        placeholders.add("arrowright", "»");
        placeholders.add("arrowleft", "«");
        placeholders.add("star", "✦");
        placeholders.add("circle", "∙");
        placeholders.add("|", "⎟");

        return placeholders.build();
    }

    public StringPlaceholders setOfflinePlaceholders(OfflinePlayer sender) {
        StringPlaceholders.Builder placeholders = StringPlaceholders.builder();

        placeholders.add("prefix", SimpleGems.getInstance().getLocaleFile().getString("prefix"));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            long gems = SimpleGems.getInstance().getGemsAPI().getGems(player);

            placeholders.add("player", player.getName());
            placeholders.add("gems", String.valueOf(gems));
            placeholders.add("gems_formatted", Methods.format(gems));
            placeholders.add("gems_decimal", Methods.formatDecimal(gems));
            placeholders.add("displayname", player.getDisplayName());
        } else {
            long gems = SimpleGems.getInstance().getGemsAPI().getOfflineGems(sender);

            placeholders.add("player", sender.getName());
            placeholders.add("gems", String.valueOf(gems));
            placeholders.add("gems_formatted", Methods.format(gems));
            placeholders.add("gems_decimal", Methods.formatDecimal(gems));
        }
        placeholders.add("arrow", "»");
        placeholders.add("arrowright", "»");
        placeholders.add("arrowleft", "«");
        placeholders.add("star", "✦");
        placeholders.add("circle", "∙");
        placeholders.add("|", "⎟");

        return placeholders.build();
    }
}