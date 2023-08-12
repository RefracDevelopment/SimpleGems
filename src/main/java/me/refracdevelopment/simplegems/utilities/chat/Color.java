package me.refracdevelopment.simplegems.utilities.chat;

import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import dev.rosewood.rosegarden.utils.HexUtils;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.configuration.LocaleManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Color {

    public static String translate(CommandSender sender, String source) {
        source = Placeholders.setPlaceholders(sender, source);

        if (sender instanceof Player && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPIHook.applyPlaceholders((Player) sender, translate(source));
        } else return translate(source);
    }

    public static String translate(String source) {
        return HexUtils.colorify(source);
    }

    public static void log(String message) {
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        String prefix = locale.getLocaleMessage("prefix");

        message = Placeholders.setPlaceholders(Bukkit.getConsoleSender(), message);

        locale.sendCustomMessage(Bukkit.getConsoleSender(), prefix + message);
    }
}