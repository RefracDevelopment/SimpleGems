package me.refracdevelopment.simplegems.commands;

import com.google.common.base.Joiner;
import me.kodysimpson.simpapi.command.SubCommand;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PayCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleGems.getInstance().getCommands().PAY_COMMAND_ALIASES.get(0);
    }

    @Override
    public List<String> getAliases() {
        return SimpleGems.getInstance().getCommands().PAY_COMMAND_ALIASES;
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-pay-description");
    }

    @Override
    public String getSyntax() {
        return "<player> <amount>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        // Make sure the sender is a player.
        if (!(commandSender instanceof Player)) {
            Color.sendMessage(commandSender, "no-console", Placeholders.setPlaceholders(commandSender));
            return;
        }

        if (strings.length == 0) {
            Color.sendCustomMessage(commandSender, getSyntax());
            return;
        }

        // note: used to prevent adding/removing negative numbers.
        if (strings[2].contains("-")) return;

        String message = Joiner.on(" ").join(strings);

        Player player = (Player) commandSender;
        OfflinePlayer target = Bukkit.getOfflinePlayer(strings[1]);

        if (!player.hasPermission(Permissions.GEMS_PAY_COMMAND)) {
            Color.sendMessage(commandSender, "no-permission");
            return;
        }

        if (target.isOnline()) {
            long amount;

            try {
                amount = Long.parseLong(strings[2]);
            } catch (NumberFormatException exception) {
                amount = 0;
                Color.sendMessage(commandSender, "invalid-number", Placeholders.setPlaceholders(commandSender));
            }

            SimpleGems.getInstance().getGemsAPI().payGems(player, target.getPlayer(), amount, message.contains("-s"));
        } else if (target.hasPlayedBefore()) {
            long amount;

            try {
                amount = Long.parseLong(strings[2]);
            } catch (NumberFormatException exception) {
                amount = 0;
                Color.sendMessage(commandSender, "invalid-number", Placeholders.setPlaceholders(commandSender));
            }

            SimpleGems.getInstance().getGemsAPI().payOfflineGems(player, target, amount);
        } else Color.sendMessage(player, "invalid-player", Placeholders.setPlaceholders(player));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }
}