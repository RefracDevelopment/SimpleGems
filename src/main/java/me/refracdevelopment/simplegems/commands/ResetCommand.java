package me.refracdevelopment.simplegems.commands;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ResetCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleGems.getInstance().getCommands().RESET_COMMAND_NAME;
    }

    @Override
    public List<String> getAliases() {
        return SimpleGems.getInstance().getCommands().RESET_COMMAND_ALIASES;
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-reset-description");
    }

    @Override
    public String getSyntax() {
        return "[player]";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        // Make sure the sender is a player.
        if (!(commandSender instanceof Player player)) {
            RyMessageUtils.sendPluginMessage(commandSender, "no-console", Placeholders.setPlaceholders(commandSender));
            return;
        }

        if (!commandSender.hasPermission(Permissions.GEMS_RESET_COMMAND)) {
            RyMessageUtils.sendPluginMessage(player, "no-permission");
            return;
        }

        if (args.length == 1) {
            switch (SimpleGems.getInstance().getDataType()) {
                case MYSQL:
                    SimpleGems.getInstance().getMySQLManager().delete();

                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.kickPlayer(RyMessageUtils.translate(p, SimpleGems.getInstance().getLocaleFile().getString("kick-messages-error")));
                    });
                    break;
                default:
                    SimpleGems.getInstance().getSqLiteManager().delete();

                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.kickPlayer(RyMessageUtils.translate(p, SimpleGems.getInstance().getLocaleFile().getString("kick-messages-error")));
                    });
                    break;
            }
        } else if (args.length == 2) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

            if (target.getPlayer() != null && target.getPlayer().isOnline()) {
                switch (SimpleGems.getInstance().getDataType()) {
                    case MYSQL:
                        SimpleGems.getInstance().getMySQLManager().deletePlayer(target.getPlayer().getUniqueId().toString());

                        Bukkit.getOnlinePlayers().forEach(p -> {
                            p.kickPlayer(RyMessageUtils.translate(p, SimpleGems.getInstance().getLocaleFile().getString("kick-messages-error")));
                        });
                        break;
                    default:
                        SimpleGems.getInstance().getSqLiteManager().deletePlayer(target.getPlayer().getUniqueId().toString());

                        Bukkit.getOnlinePlayers().forEach(p -> {
                            p.kickPlayer(RyMessageUtils.translate(p, SimpleGems.getInstance().getLocaleFile().getString("kick-messages-error")));
                        });
                        break;
                }
            } else if (target.hasPlayedBefore()) {
                switch (SimpleGems.getInstance().getDataType()) {
                    case MYSQL:
                        SimpleGems.getInstance().getMySQLManager().deletePlayer(target.getPlayer().getUniqueId().toString());

                        RyMessageUtils.sendPluginMessage(player, "gems-reset-player");
                        break;
                    default:
                        SimpleGems.getInstance().getSqLiteManager().deletePlayer(target.getPlayer().getUniqueId().toString());

                        RyMessageUtils.sendPluginMessage(player, "gems-reset-player");
                        break;
                }
            }
        }
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