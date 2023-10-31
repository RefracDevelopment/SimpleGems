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

public class TakeCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleGems.getInstance().getCommands().TAKE_COMMAND_NAME;
    }

    @Override
    public List<String> getAliases() {
        return SimpleGems.getInstance().getCommands().TAKE_COMMAND_ALIASES;
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-take-description");
    }

    @Override
    public String getSyntax() {
        return "<player> <amount>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        // note: used to prevent adding/removing negative numbers.
        if (strings[2].contains("-")) return;

        String message = Joiner.on(" ").join(strings);

        OfflinePlayer target = Bukkit.getOfflinePlayer(strings[1]);

        if (!commandSender.hasPermission(Permissions.GEMS_TAKE_COMMAND)) {
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

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(player))
                    .add("player", player.getName())
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            if (!SimpleGems.getInstance().getGemsAPI().hasGems(player, amount)) {
                Color.sendMessage(commandSender, "invalid-gems", placeholders);
                return;
            }

            SimpleGems.getInstance().getGemsAPI().takeGems(player, amount);

            Color.sendMessage(commandSender, "gems-taken", placeholders);
            if (message.contains("-s")) return;
            Color.sendMessage(player, "gems-lost", placeholders);
        } else if (target.hasPlayedBefore()) {
            long amount;

            try {
                amount = Long.parseLong(strings[2]);
            } catch (NumberFormatException exception) {
                amount = 0;
                Color.sendMessage(commandSender, "invalid-number", Placeholders.setPlaceholders(commandSender));
            }

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(commandSender))
                    .add("player", target.getName())
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            if (!SimpleGems.getInstance().getGemsAPI().hasOfflineGems(target, amount)) {
                Color.sendMessage(commandSender, "invalid-gems", placeholders);
                return;
            }

            SimpleGems.getInstance().getGemsAPI().takeOfflineGems(target, amount);

            Color.sendMessage(commandSender, "gems-taken", placeholders);
        } else Color.sendMessage(commandSender, "invalid-player");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }
}
