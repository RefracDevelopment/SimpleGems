package me.refracdevelopment.simplegems.plugin.utilities.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.utilities.VersionCheck;
import me.refracdevelopment.simplegems.plugin.utilities.files.Messages;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class Color {

    private static final Pattern HEX_PATTERN = Pattern.compile("(&#[0-9a-fA-F]{6})");

    public static String translate(Player player, String source) {
        source = Placeholders.setPlaceholders(player, source);

        if (SimpleGems.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, Color.translate(source));
        } else return Color.translate(source);
    }

    public static String translate(String source) {
        String hexColored = source;

        if (VersionCheck.isOnePointSixteenPlus()) {
            Matcher matcher = HEX_PATTERN.matcher(source);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String hex = matcher.group(1).substring(1);
                matcher.appendReplacement(sb, net.md_5.bungee.api.ChatColor.of(hex) + "");
            }
            matcher.appendTail(sb);

            hexColored = sb.toString();
        }

        return org.bukkit.ChatColor.translateAlternateColorCodes('&', hexColored);
    }

    public static List<String> translate(List<String> source) {
        return source.stream().map(Color::translate).collect(Collectors.toList());
    }

    public static void sendMessage(CommandSender sender, String source, boolean color, boolean placeholders) {
        if (source.equalsIgnoreCase("%empty%") || source.contains("%empty%")) return;
        if (placeholders) source = Placeholders.setPlaceholders(sender, source);

        if (sender instanceof Player) {
            if (SimpleGems.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                source = PlaceholderAPI.setPlaceholders((Player) sender, source);
            }
        }

        if (color) source = translate(source);

        sender.sendMessage(source);
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(translate(Messages.PREFIX
                .replace("%arrow%", StringEscapeUtils.unescapeJava("\u00BB"))
                .replace("%arrow_2%", StringEscapeUtils.unescapeJava("\u27A5"))
                .replace("%star%", StringEscapeUtils.unescapeJava("\u2726"))
                .replace("%circle%", StringEscapeUtils.unescapeJava("\u2219"))
                .replace("|", StringEscapeUtils.unescapeJava("\u2503"))
                + " " + message
        ));
    }
}