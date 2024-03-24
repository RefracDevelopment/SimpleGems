package me.refracdevelopment.simplegems.commands;

import com.google.common.base.Joiner;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.chat.StringPlaceholders;
import me.refracdevelopment.simplegems.utilities.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleGems.getInstance().getCommands().SET_COMMAND_NAME;
    }

    @Override
    public List<String> getAliases() {
        return SimpleGems.getInstance().getCommands().SET_COMMAND_ALIASES;
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-set-description");
    }

    @Override
    public String getSyntax() {
        return "<player> <amount> [-s]";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (!(args.length >= 3)) {
            String baseColor = SimpleGems.getInstance().getLocaleFile().getString("base-command-color");
            Color.sendCustomMessage(commandSender, baseColor + "/" + SimpleGems.getInstance().getCommands().GEMS_COMMAND_NAME + " " + getName() + " " + getSyntax());
            return;
        }

        // note: used to prevent adding/removing negative numbers.
        if (args[2].contains("-")) {
            Color.sendMessage(commandSender, "invalid-number");
            return;
        }

        String message = Joiner.on(" ").join(args);

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if (!commandSender.hasPermission(Permissions.GEMS_SET_COMMAND)) {
            Color.sendMessage(commandSender, "no-permission");
            return;
        }

        if (target.isOnline()) {
            Player player = target.getPlayer();
            long amount;

            try {
                amount = Long.parseLong(args[2]);
            } catch (NumberFormatException exception) {
                Color.sendMessage(commandSender, "invalid-number");
                return;
            }

            SimpleGems.getInstance().getGemsAPI().setGems(player, amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(player))
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            Color.sendMessage(commandSender, "gems-set", placeholders);
            if (message.contains("-s")) return;
            Color.sendMessage(player, "gems-setted", placeholders);
        } else if (target.hasPlayedBefore()) {
            long amount;

            try {
                amount = Long.parseLong(args[2]);
            } catch (NumberFormatException exception) {
                Color.sendMessage(commandSender, "invalid-number");
                return;
            }

            SimpleGems.getInstance().getGemsAPI().setOfflineGems(target, amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setOfflinePlaceholders(target))
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            if (message.contains("-s")) return;
            Color.sendMessage(commandSender, "gems-set", placeholders);
        } else
            Color.sendMessage(commandSender, "invalid-player");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> names = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(p -> names.add(p.getName()));

        if (args.length == 2)
            return names;

        return null;
    }

}