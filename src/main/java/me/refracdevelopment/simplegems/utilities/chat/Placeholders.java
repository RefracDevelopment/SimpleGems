package me.refracdevelopment.simplegems.utilities.chat;

import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.data.Profile;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.utilities.Methods;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Placeholders {

    public static String setPlaceholders(CommandSender sender, String placeholder) {
        placeholder = placeholder.replace("%prefix%", SimpleGems.getInstance().getManager(LocaleManager.class).getLocaleMessage("prefix"));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

            placeholder = placeholder.replace("%player%", player.getName());
            placeholder = placeholder.replace("%gems%", String.valueOf(profile.getData().getGems().getAmount()));
            placeholder = placeholder.replace("%gems_formatted%", Methods.format(profile.getData().getGems().getAmount()));
            placeholder = placeholder.replace("%gems_decimal%", Methods.formatDec(profile.getData().getGems().getAmount()));
            placeholder = placeholder.replace("%displayname%", player.getDisplayName());
        }
        placeholder = placeholder.replace("%arrow%", "\u00BB");
        placeholder = placeholder.replace("%arrow2%", "\u27A5");
        placeholder = placeholder.replace("%arrow_2%", "\u27A5");
        placeholder = placeholder.replace("%star%", "\u2726");
        placeholder = placeholder.replace("%circle%", "\u2219");
        placeholder = placeholder.replace("|", "\u239F");

        return placeholder;
    }

    public static StringPlaceholders setPlaceholders(CommandSender sender) {
        StringPlaceholders placeholders = StringPlaceholders.builder().build();

        placeholders.addPlaceholder("prefix", SimpleGems.getInstance().getManager(LocaleManager.class).getLocaleMessage("prefix"));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

            placeholders.addPlaceholder("player", player.getName());
            placeholders.addPlaceholder("gems", String.valueOf(profile.getData().getGems().getAmount()));
            placeholders.addPlaceholder("gems_formatted", Methods.format(profile.getData().getGems().getAmount()));
            placeholders.addPlaceholder("gems_decimal", Methods.formatDec(profile.getData().getGems().getAmount()));
            placeholders.addPlaceholder("displayname", player.getDisplayName());
        }
        placeholders.addPlaceholder("arrow", "\u00BB");
        placeholders.addPlaceholder("arrow2", "\u27A5");
        placeholders.addPlaceholder("arrow_2", "\u27A5");
        placeholders.addPlaceholder("star", "\u2726");
        placeholders.addPlaceholder("circle", "\u2219");
        placeholders.addPlaceholder("|", "\u239F");

        return placeholders;
    }
}