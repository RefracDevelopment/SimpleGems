package me.refracdevelopment.simplegems.plugin.utilities.chat;

import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.manager.Profile;
import me.refracdevelopment.simplegems.plugin.utilities.Methods;
import me.refracdevelopment.simplegems.plugin.utilities.files.Messages;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class Placeholders {

    public static String setPlaceholders(CommandSender sender, String placeholder) {
        placeholder = placeholder.replace("%prefix%", Messages.PREFIX);
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

            placeholder = placeholder.replace("%player%", player.getName());
            placeholder = placeholder.replace("%gems%", Methods.format(profile.getData().getGems().getStat()));
            placeholder = placeholder.replace("%gems_decimal%", Methods.formatDec(profile.getData().getGems().getStat()));
            placeholder = placeholder.replace("%displayname%", player.getDisplayName());
        }
        placeholder = placeholder.replace("%arrow%", StringEscapeUtils.unescapeJava("\u00BB"));
        placeholder = placeholder.replace("%arrow_2%", StringEscapeUtils.unescapeJava("\u27A5"));
        placeholder = placeholder.replace("%star%", StringEscapeUtils.unescapeJava("\u2726"));
        placeholder = placeholder.replace("%circle%", StringEscapeUtils.unescapeJava("\u2219"));
        placeholder = placeholder.replace("|", StringEscapeUtils.unescapeJava("\u2503"));

        return placeholder;
    }
}