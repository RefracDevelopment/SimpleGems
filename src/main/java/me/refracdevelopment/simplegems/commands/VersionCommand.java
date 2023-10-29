package me.refracdevelopment.simplegems.commands;

import me.kodysimpson.simpapi.command.SubCommand;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.StringPlaceholders;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VersionCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleGems.getInstance().getCommands().VERSION_COMMAND_ALIASES.get(0);
    }

    @Override
    public List<String> getAliases() {
        return SimpleGems.getInstance().getCommands().VERSION_COMMAND_ALIASES;
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-version-description");
    }

    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (!commandSender.hasPermission(Permissions.GEMS_VERSION_COMMAND)) {
            Color.sendMessage(commandSender, "no-permission");
            return;
        }

        String baseColor = SimpleGems.getInstance().getLocaleFile().getString("base-command-color");
        Color.sendCustomMessage(commandSender, baseColor + "Running <g:#8A2387:#E94057:#F27121>" + SimpleGems.getInstance().getDescription().getName() + baseColor + " v" + SimpleGems.getInstance().getDescription().getVersion());
        Color.sendCustomMessage(commandSender, baseColor + "Plugin created by: <g:#41E0F0:#FF8DCE>" + SimpleGems.getInstance().getDescription().getAuthors().get(0));
        Color.sendMessage(commandSender, "base-command-help", StringPlaceholders.of("cmd", SimpleGems.getInstance().getCommands().GEMS_COMMAND_ALIASES.get(0)));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }
}