package me.refracdevelopment.simplegems.commands;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WithdrawCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleGems.getInstance().getCommands().WITHDRAW_COMMAND_NAME;
    }

    @Override
    public List<String> getAliases() {
        return SimpleGems.getInstance().getCommands().WITHDRAW_COMMAND_ALIASES;
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-withdraw-description");
    }

    @Override
    public String getSyntax() {
        return "<amount>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        // Make sure the sender is a player.
        if (!(commandSender instanceof Player)) {
            Color.sendMessage(commandSender, "no-console", Placeholders.setPlaceholders(commandSender));
            return;
        }

        Player player = (Player) commandSender;

        if (strings.length == 1) {
            String baseColor = SimpleGems.getInstance().getLocaleFile().getString("base-command-color");
            Color.sendCustomMessage(commandSender, baseColor + "/" + SimpleGems.getInstance().getCommands().GEMS_COMMAND_NAME + " " + getSyntax());
            return;
        }

        if (!player.hasPermission(Permissions.GEMS_WITHDRAW_COMMAND)) {
            Color.sendMessage(commandSender, "no-permission");
            return;
        }

        // note: used to prevent adding/removing negative numbers.
        if (strings[1].contains("-")) return;

        long amount;

        try {
            amount = Long.parseLong(strings[1]);
        } catch (NumberFormatException exception) {
            amount = 0;
            Color.sendMessage(commandSender, "invalid-number", Placeholders.setPlaceholders(commandSender));
        }

        SimpleGems.getInstance().getGemsAPI().withdrawGems(player, amount);
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }
}