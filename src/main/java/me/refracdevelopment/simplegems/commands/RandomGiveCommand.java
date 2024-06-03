package me.refracdevelopment.simplegems.commands;

import com.google.common.base.Joiner;
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
import java.util.concurrent.ThreadLocalRandom;

public class RandomGiveCommand extends SubCommand {
    @Override
    public String getName() {
        return SimpleGems.getInstance().getCommands().RANDOM_GIVE_COMMAND_NAME;
    }

    @Override
    public List<String> getAliases() {
        return SimpleGems.getInstance().getCommands().RANDOM_GIVE_COMMAND_ALIASES;
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-random-give-description");
    }

    @Override
    public String getSyntax() {
        return "<player> <min> <max>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        // note: used to prevent adding/removing negative numbers.
        if (args[2].contains("-") || args[3].contains("-"))
            return;

        String message = Joiner.on(" ").join(args);

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if (!commandSender.hasPermission(Permissions.GEMS_GIVE_COMMAND)) {
            RyMessageUtils.sendSender(commandSender, "no-permission");
            return;
        }

        if (target.isOnline()) {
            Player targetPlayer = target.getPlayer();

            double min;
            double max;
            double amount;

            try {
                min = Double.parseDouble(args[2]);
                max = Double.parseDouble(args[3]);
                amount = ThreadLocalRandom.current().nextDouble(min, max);
            } catch (NumberFormatException exception) {
                RyMessageUtils.sendSender(commandSender, "invalid-number");
                return;
            }

            if (commandSender instanceof Player player)
                SimpleGems.getInstance().getGemsAPI().giveGems(player, targetPlayer, amount);
            else
                SimpleGems.getInstance().getGemsAPI().giveGems(targetPlayer, amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(targetPlayer))
                    .add("player", targetPlayer.getName())
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            if (message.contains("-s"))
                return;

            RyMessageUtils.sendPluginMessage(commandSender, "gems-given", placeholders);
            RyMessageUtils.sendPluginMessage(targetPlayer, "gems-gained", placeholders);
        } else if (target.hasPlayedBefore()) {
            double min;
            double max;
            double amount;

            try {
                min = Double.parseDouble(args[2]);
                max = Double.parseDouble(args[3]);
                amount = ThreadLocalRandom.current().nextDouble(min, max);
            } catch (NumberFormatException exception) {
                RyMessageUtils.sendSender(commandSender, "invalid-number");
                return;
            }

            SimpleGems.getInstance().getGemsAPI().giveOfflineGems(target, amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(commandSender))
                    .add("player", target.getName())
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            RyMessageUtils.sendPluginMessage(commandSender, "gems-given", placeholders);
        } else
            RyMessageUtils.sendPluginMessage(commandSender, "invalid-player", Placeholders.setPlaceholders(commandSender));
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
