package me.refracdevelopment.simplegems.plugin.utilities.chat;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import me.refracdevelopment.simplegems.plugin.SimpleGems;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class Color {

    public static String translate(Player player, String source) {
        source = Placeholders.setPlaceholders(player, source);

        if (SimpleGems.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, Color.translate(source));
        } else return Color.translate(source);
    }

    public static String translate(String source) {
        source = IridiumColorAPI.process(source);

        return ChatColor.translateAlternateColorCodes('&', source);
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
}