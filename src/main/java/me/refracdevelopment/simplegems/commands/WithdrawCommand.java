package me.refracdevelopment.simplegems.commands;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
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
    public void perform(CommandSender commandSender, String[] args) {
        // Make sure the sender is a player.
        if (!(commandSender instanceof Player player)) {
            RyMessageUtils.sendPluginMessage(commandSender, "no-console", Placeholders.setPlaceholders(commandSender));
            return;
        }


        if (args.length != 2) {
            String baseColor = SimpleGems.getInstance().getLocaleFile().getString("base-command-color");
            RyMessageUtils.sendSender(commandSender, baseColor + "/" + SimpleGems.getInstance().getCommands().GEMS_COMMAND_NAME + " " + getName() + " " + getSyntax());
            return;
        }

        if (!player.hasPermission(Permissions.GEMS_WITHDRAW_COMMAND)) {
            RyMessageUtils.sendPluginMessage(commandSender, "no-permission");
            return;
        }

        // note: used to prevent adding/removing negative numbers.
        if (args[1].contains("-")) {
            RyMessageUtils.sendPluginMessage(commandSender, "invalid-number", Placeholders.setPlaceholders(commandSender));
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException exception) {
            RyMessageUtils.sendPluginMessage(commandSender, "invalid-number", Placeholders.setPlaceholders(commandSender));
            return;
        }

        SimpleGems.getInstance().getGemsAPI().withdrawGems(player, amount);
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return List.of();
    }
}