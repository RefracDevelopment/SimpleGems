package me.refracdevelopment.simplegems.commands;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
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
            RyMessageUtils.sendPluginMessage(commandSender, "no-permission");
            return;
        }

        if (args.length != 2) {
            if (!(commandSender instanceof Player))
                return;

            RyMessageUtils.sendPluginMessage(commandSender, "gems-balance");
            return;
        }

        if (Bukkit.getPlayer(args[1]) != null) {
            Player target = Bukkit.getPlayer(args[1]);
            RyMessageUtils.sendPluginMessage(commandSender, "gems-balance", Placeholders.setPlaceholders(target));
        } else if (Bukkit.getOfflinePlayer(args[1]) != null && Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            double amount = SimpleGems.getInstance().getGemsAPI().getOfflineGems(target);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setOfflinePlaceholders(target))
                    .add("player", target.getName())
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            RyMessageUtils.sendPluginMessage(commandSender, "gems-balance", placeholders);
        } else
            RyMessageUtils.sendPluginMessage(commandSender, "invalid-player");
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