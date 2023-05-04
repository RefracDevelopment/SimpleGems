package me.refracdevelopment.simplegems.utilities.chat;

import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.utilities.Methods;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Placeholders {

    public static String setPlaceholders(CommandSender sender, String placeholder) {
        placeholder = placeholder.replace("%prefix%", SimpleGems.getInstance().getManager(LocaleManager.class).getLocaleMessage("prefix"));
        if (sender instanceof Player) {
            Player player = (Player) sender;

            placeholder = placeholder.replace("%player%", player.getName());
            placeholder = placeholder.replace("%gems%", String.valueOf(SimpleGemsAPI.INSTANCE.getGems(player)));
            placeholder = placeholder.replace("%gems_formatted%", Methods.format(SimpleGemsAPI.INSTANCE.getGems(player)));
            placeholder = placeholder.replace("%gems_decimal%", Methods.formatDec(SimpleGemsAPI.INSTANCE.getGems(player)));
            placeholder = placeholder.replace("%displayname%", player.getDisplayName());
        }
        placeholder = placeholder.replace("%arrow%", "\u00BB");
        placeholder = placeholder.replace("%arrowright%", "\u00BB");
        placeholder = placeholder.replace("%arrowleft%", "\u00AB");
        placeholder = placeholder.replace("%star%", "\u2726");
        placeholder = placeholder.replace("%circle%", "\u2219");
        placeholder = placeholder.replace("|", "\u239F");

        return placeholder;
    }

    public static StringPlaceholders setPlaceholders(CommandSender sender) {
        StringPlaceholders.Builder placeholders = StringPlaceholders.builder();

        placeholders.add("prefix", SimpleGems.getInstance().getManager(LocaleManager.class).getLocaleMessage("prefix"));
        if (sender instanceof Player) {
            Player player = (Player) sender;

            placeholders.add("player", player.getName());
            placeholders.add("gems", String.valueOf(SimpleGemsAPI.INSTANCE.getGems(player)));
            placeholders.add("gems_formatted", Methods.format(SimpleGemsAPI.INSTANCE.getGems(player)));
            placeholders.add("gems_decimal", Methods.formatDec(SimpleGemsAPI.INSTANCE.getGems(player)));
            placeholders.add("displayname", player.getDisplayName());
        }
        placeholders.add("arrow", "\u00BB");
        placeholders.add("arrowright", "\u00BB");
        placeholders.add("arrowleft", "\u00AB");
        placeholders.add("star", "\u2726");
        placeholders.add("circle", "\u2219");
        placeholders.add("|", "\u239F");

        return placeholders.build();
    }
}