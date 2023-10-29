package me.refracdevelopment.simplegems.commands;

import me.kodysimpson.simpapi.command.SubCommand;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class UpdateCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleGems.getInstance().getCommands().UPDATE_COMMAND_ALIASES.get(0);
    }

    @Override
    public List<String> getAliases() {
        return SimpleGems.getInstance().getCommands().UPDATE_COMMAND_ALIASES;
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-update-description");
    }

    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (!commandSender.hasPermission(Permissions.GEMS_UPDATE_COMMAND)) {
            Color.sendMessage(commandSender, "no-permission");
            return;
        }

        SimpleGems.getInstance().getLeaderboardManager().update();
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }
}