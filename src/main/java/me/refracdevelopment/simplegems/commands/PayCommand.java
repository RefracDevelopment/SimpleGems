package me.refracdevelopment.simplegems.commands;

import com.google.common.base.Joiner;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PayCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleGems.getInstance().getCommands().PAY_COMMAND_NAME;
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
        return "<player> <amount> [-s]";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        // Make sure the sender is a player.
        if (!(commandSender instanceof Player)) {
            Color.sendMessage(commandSender, "no-console", Placeholders.setPlaceholders(commandSender));
            return;
        }

        if (!(args.length >= 3)) {
            String baseColor = SimpleGems.getInstance().getLocaleFile().getString("base-command-color");
            Color.sendCustomMessage(commandSender, baseColor + "/" + SimpleGems.getInstance().getCommands().GEMS_COMMAND_NAME + " " + getName() + " " +  getSyntax());
            return;
        }

        // note: used to prevent adding/removing negative numbers.
        if (args[2].contains("-")) {
            Color.sendMessage(commandSender, "invalid-number", Placeholders.setPlaceholders(commandSender));
            return;
        }

        String message = Joiner.on(" ").join(args);

        Player player = (Player) commandSender;
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if (!player.hasPermission(Permissions.GEMS_PAY_COMMAND)) {
            Color.sendMessage(commandSender, "no-permission");
            return;
        }

        if (target.isOnline()) {
            long amount;

            try {
                amount = Long.parseLong(args[2]);
            } catch (NumberFormatException exception) {
                Color.sendMessage(commandSender, "invalid-number", Placeholders.setPlaceholders(commandSender));
                return;
            }

            SimpleGems.getInstance().getGemsAPI().payGems(player, target.getPlayer(), amount, message.contains("-s"));
        } else if (target.hasPlayedBefore()) {
            long amount;

            try {
                amount = Long.parseLong(args[2]);
            } catch (NumberFormatException exception) {
                Color.sendMessage(commandSender, "invalid-number", Placeholders.setPlaceholders(commandSender));
                return;
            }

            SimpleGems.getInstance().getGemsAPI().payOfflineGems(player, target, amount);
        } else
            Color.sendMessage(player, "invalid-player");
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