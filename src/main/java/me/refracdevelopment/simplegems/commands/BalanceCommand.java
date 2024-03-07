package me.refracdevelopment.simplegems.commands;

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

public class BalanceCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleGems.getInstance().getCommands().BALANCE_COMMAND_NAME;
    }

    @Override
    public List<String> getAliases() {
        return SimpleGems.getInstance().getCommands().BALANCE_COMMAND_ALIASES;
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-balance-description");
    }

    @Override
    public String getSyntax() {
        return "[player]";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission(Permissions.GEMS_BALANCE_COMMAND)) {
            Color.sendMessage(commandSender, "no-permission");
            return;
        }

        if (args.length != 2) {
            Color.sendMessage(commandSender, "gems-balance");
            return;
        }

        if (Bukkit.getPlayer(args[1]) != null) {
            Player target = Bukkit.getPlayer(args[1]);
            Color.sendMessage(commandSender, "gems-balance", Placeholders.setPlaceholders(target));
        } else if (Bukkit.getOfflinePlayer(args[1]) != null && Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            long amount = SimpleGems.getInstance().getGemsAPI().getOfflineGems(target);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setOfflinePlaceholders(target))
                    .add("player", target.getName())
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            Color.sendMessage(commandSender, "gems-balance", placeholders);
        } else Color.sendMessage(commandSender, "invalid-player", Placeholders.setPlaceholders(commandSender));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> names = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(p -> names.add(p.getName()));

        if (args.length == 2) {
            return names;
        }
        return null;
    }
}