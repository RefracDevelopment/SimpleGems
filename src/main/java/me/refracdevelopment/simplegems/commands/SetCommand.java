package me.refracdevelopment.simplegems.commands;

import com.google.common.base.Joiner;
import me.kodysimpson.simpapi.command.SubCommand;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.chat.StringPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        return "<player> <amount>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            Color.sendCustomMessage(commandSender, getSyntax());
            return;
        }

        // note: used to prevent adding/removing negative numbers.
        if (strings[2].contains("-")) return;

        String message = Joiner.on(" ").join(strings);

        OfflinePlayer target = Bukkit.getOfflinePlayer(strings[1]);

        if (!commandSender.hasPermission(Permissions.GEMS_SET_COMMAND)) {
            Color.sendMessage(commandSender, "no-permission");
            return;
        }

        if (target.isOnline()) {
            Player player = target.getPlayer();
            long amount;

            try {
                amount = Long.parseLong(strings[2]);
            } catch (NumberFormatException exception) {
                amount = 0;
                Color.sendMessage(commandSender, "invalid-number", Placeholders.setPlaceholders(commandSender));
            }

            SimpleGems.getInstance().getGemsAPI().setGems(player, amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(player))
                    .add("player", player.getName())
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
                amount = Long.parseLong(strings[2]);
            } catch (NumberFormatException exception) {
                amount = 0;
                Color.sendMessage(commandSender, "invalid-number", Placeholders.setPlaceholders(commandSender));
            }

            SimpleGems.getInstance().getGemsAPI().setOfflineGems(target, amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(commandSender))
                    .add("player", target.getName())
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            Color.sendMessage(commandSender, "gems-set", placeholders);
        } else Color.sendMessage(commandSender, "invalid-player", Placeholders.setPlaceholders(commandSender));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }

}